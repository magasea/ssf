package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.BonusInfo;
import com.shellshellfish.aaas.userinfo.model.ConfirmResult;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.FundInfo;
import com.shellshellfish.aaas.userinfo.model.FundShare;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.CoinFundYieldRate;
import com.shellshellfish.aaas.userinfo.model.dao.FundYieldRate;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.repositories.funds.MongoCoinFundYieldRateRepository;
import com.shellshellfish.aaas.userinfo.repositories.funds.MongoFundYieldRateRepository;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.FundTradeApiService;
import com.shellshellfish.aaas.userinfo.service.RpcOrderService;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.bytebuddy.asm.Advice.Unused;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class UserFinanceProdCalcServiceImpl implements UserFinanceProdCalcService {

	private static final Logger logger = LoggerFactory
			.getLogger(UserFinanceProdCalcServiceImpl.class);

	@Autowired
	private UiProductRepo uiProductRepo;

	@Autowired
	private UiProductDetailRepo uiProductDetailRepo;

	@Autowired
	private FundTradeApiService fundTradeApiService;

	@Autowired
	@Qualifier("zhongZhengMongoTemplate")
	private MongoTemplate zhongZhengMongoTemplate;

	@Value("${daily-finance-calculate-thread:10}")
	private int threadNum;

	@Autowired
	private UserInfoRepository userInfoRepository;


	@Autowired
	UserInfoRepoService userInfoRepoService;

	@Autowired
	UserInfoBankCardsRepository userInfoBankCardsRepository;

	@Autowired
	MongoFundYieldRateRepository mongoFundYieldRateRepository;

	@Autowired
	MongoCoinFundYieldRateRepository mongoCoinFundYieldRateRepository;

	@Autowired
	@Qualifier("mongoTemplate")
	private MongoTemplate mongoTemplate;


	@Autowired
	MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;


	@Autowired
	RpcOrderService rpcOrderService;

	@Override
	public BigDecimal calcTotalDailyAsset(String userUuid) throws Exception {
		BigDecimal totalDailyAsset = BigDecimal.ZERO;
		List<UiProducts> userProducts = uiProductRepo.findAll();
		for (UiProducts prod : userProducts) {
			List<UiProductDetail> prodDetails = uiProductDetailRepo.findAllByUserProdId(prod.getId());
			for (UiProductDetail detail : prodDetails) {
				String fundCode = detail.getFundCode();
				initDailyAmount(userUuid, prod.getProdId(), detail.getUserProdId(), getTodayAsString(),
						fundCode);
				BigDecimal asset = calcDailyAsset(userUuid, prod.getProdId(), fundCode, getTodayAsString());
				totalDailyAsset.add(asset);
			}
		}
		return totalDailyAsset;
	}

	@Override
	public BigDecimal calcDailyAsset(String userUuid, Long prodId, String fundCode, String date)
			throws Exception {
		FundShare fundShare = fundTradeApiService.getFundShare(userUuid, fundCode);
		if (fundShare == null) {
			return BigDecimal.ZERO;
		}
		FundInfo fundInfo = fundTradeApiService.getFundInfoAsEntity(fundCode);
		BigDecimal share = new BigDecimal(fundShare.getUsableremainshare());
		BigDecimal netValue = new BigDecimal(fundInfo.getPernetvalue());
		BigDecimal rateOfSellFund = fundTradeApiService.getRate(fundCode, "024");

		BigDecimal fundAsset = share.multiply(netValue)
				.multiply(BigDecimal.ONE.subtract(rateOfSellFund));

		String today = date;//getTodayAsString();

		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").is(today))
				.addCriteria(Criteria.where("fundCode").is(fundCode))
				.addCriteria(Criteria.where("prodId").is(prodId));

		Update update = new Update();
		update.set("userUuid", userUuid);
		update.set("prodId", prodId);
		update.set("fundCode", fundCode);
		update.set("asset", fundAsset);

		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.upsert(true);
		DailyAmount dailyAmount = zhongZhengMongoTemplate
				.findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
		logger.info("dailyAmount:{}", dailyAmount);

		return fundAsset;
	}

	private BigDecimal calcDailyAsset2(String userUuid, Long prodId, Long userProdId, String fundCode,
			String date, UiProductDetail uiProductDetail) throws Exception {
		BigDecimal share = new BigDecimal(uiProductDetail.getFundQuantity());

		if (BigDecimal.ZERO.equals(share)) {
			return BigDecimal.ZERO;
		}
		share = share.divide(BigDecimal.valueOf(100));

		LocalDate localDate = InstantDateUtil.format(date, "yyyyMMdd");
		localDate.plusDays(1);

		BigDecimal netValue;

		if (MonetaryFundEnum.containsCode(fundCode)) {
			//货币基金使用附权单位净值
			CoinFundYieldRate coinFundYieldRate = mongoCoinFundYieldRateRepository
					.findFirstByCodeAndQueryDateBefore(fundCode,
							InstantDateUtil.getEpochSecondOfZero(localDate),
							new Sort(new Order(Direction.DESC, "querydate")));
			if (coinFundYieldRate == null || coinFundYieldRate.getNavadj() == null) {
				return BigDecimal.ZERO;
			}
			netValue = coinFundYieldRate.getNavadj();
		} else {
			FundYieldRate fundYieldRate = mongoFundYieldRateRepository
					.findFirstByCodeAndQueryDateBefore(fundCode,
							InstantDateUtil.getEpochSecondOfZero(localDate),
							new Sort(new Order(Direction.DESC, "querydate")));

			if (fundYieldRate == null || fundYieldRate.getUnitNav() == null) {
				return BigDecimal.ZERO;
			}
			netValue = fundYieldRate.getUnitNav();
		}

		BigDecimal rateOfSellFund = fundTradeApiService.getRate(fundCode, "024");

		BigDecimal fundAsset = share.multiply(netValue)
				.multiply(BigDecimal.ONE.subtract(rateOfSellFund));

		String today = date;//getTodayAsString();

		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").is(today))
				.addCriteria(Criteria.where("fundCode").is(fundCode))
				.addCriteria(Criteria.where("prodId").is(prodId))
				.addCriteria(Criteria.where("userProdId").is(userProdId));

		Update update = new Update();
		update.set("asset", fundAsset);

		DailyAmount dailyAmount = zhongZhengMongoTemplate
				.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true),
						DailyAmount.class);
		logger.info(
				"set asset ==> dailyAmount:{}", dailyAmount);

		return fundAsset;
	}

	private String getTodayAsString() {
		final Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return simpleDateFormat.format(cal.getTime());
	}

	private String getYesterdayAsString() {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(TradeUtil.getUTCTime1DayBefore());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		return simpleDateFormat.format(cal.getTime());
	}

	@Override
	public void calcIntervalAmount(String userUuid, Long prodId, String fundCode, String startDate)
			throws Exception {
		List<BonusInfo> bonusInfoList = fundTradeApiService.getBonusList(userUuid, fundCode, startDate);
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.upsert(true);

		for (BonusInfo info : bonusInfoList) {
			Query query = new Query();
			query.addCriteria(Criteria.where("userUuid").is(userUuid))
					.addCriteria(Criteria.where("date").is(info.getConfirmdate()))
					.addCriteria(Criteria.where("fundCode").is(fundCode))
					.addCriteria(Criteria.where("prodId").is(prodId));

			Update update = new Update();
			update.set("userUuid", userUuid);
			update.set("prodId", prodId);
			update.set("fundCode", fundCode);
			update.set("bonus", info.getFactbonussum());
			DailyAmount dailyAmount = zhongZhengMongoTemplate
					.findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
			logger.info("dailyAmount:{}", dailyAmount);
		}

		List<ConfirmResult> confirmList = fundTradeApiService
				.getConfirmResults(userUuid, fundCode, null);
		for (ConfirmResult result : confirmList) {
			Query query = new Query();
			query.addCriteria(Criteria.where("userUuid").is(userUuid))
					.addCriteria(Criteria.where("date").is(result.getConfirmdate()))
					.addCriteria(Criteria.where("fundCode").is(fundCode))
					.addCriteria(Criteria.where("prodId").is(prodId));

			Update update = new Update();
			update.set("userUuid", userUuid);
			update.set("prodId", prodId);
			update.set("fundCode", fundCode);
			if ("022".equals(result.getCallingcode())) {
				update.set("buyAmount", result.getTradeconfirmsum());
			} else if ("024".equals(result.getCallingcode())) {
				update.set("sellAmount", result.getTradeconfirmsum());
			}

			DailyAmount dailyAmount = zhongZhengMongoTemplate
					.findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
			logger.info("dailyAmount:{}", dailyAmount);
		}
	}


	private void calcIntervalAmount2(String userUuid, Long prodId, Long userProdId,
			String fundCode, String startDate) {

		//FIXME 此处缺少分红

		MongoUiTrdZZInfo mongoUiTrdZZInfoBuy = mongoUiTrdZZInfoRepo
				.findFirstByUserProdIdAndTradeTypeAndTradeStatusAndConfirmDate(userProdId,
						TrdOrderOpTypeEnum.BUY.getOperation(), TrdOrderStatusEnum.CONFIRMED.getStatus(),
						startDate);

		MongoUiTrdZZInfo mongoUiTrdZZInfoSell = mongoUiTrdZZInfoRepo
				.findFirstByUserProdIdAndTradeTypeAndTradeStatusAndConfirmDate(userProdId,
						TrdOrderOpTypeEnum.REDEEM.getOperation(), TrdOrderStatusEnum.CONFIRMED.getStatus(),
						startDate);

		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").is(startDate))
				.addCriteria(Criteria.where("fundCode").is(fundCode))
				.addCriteria(Criteria.where("prodId").is(prodId))
				.addCriteria(Criteria.where("userProdId").is(userProdId));

		Update update = new Update();

		Optional<MongoUiTrdZZInfo> buy = Optional.ofNullable(mongoUiTrdZZInfoBuy);
		Optional<MongoUiTrdZZInfo> sell = Optional.ofNullable(mongoUiTrdZZInfoSell);

		update
				.set("buyAmount",
						Double.valueOf(sell.map(m -> m.getTradeConfirmSum()).map(m -> m / 100L).orElse(0L)));

		update.set("sellAmount",
				Double.valueOf(buy.map(m -> m.getTradeConfirmSum()).map(m -> m / 100L).orElse(0L)));

		DailyAmount dailyAmount = zhongZhengMongoTemplate
				.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true),
						DailyAmount.class);
		logger.info(
				"set buyAmount and sell Amount ==> dailyAmount:{}", dailyAmount);

	}

	@Override
	public void initDailyAmount(String userUuid, Long prodId, Long userProdId, String date,
			String fundCode) {

		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").is(date))
				.addCriteria(Criteria.where("fundCode").is(fundCode))
				.addCriteria(Criteria.where("prodId").is(prodId))
				.addCriteria(Criteria.where("userProdId").is(userProdId));

		Update update = new Update();
		update.set("asset", BigDecimal.ZERO);
		update.set("bonus", BigDecimal.ZERO);
		update.set("buyAmount", BigDecimal.ZERO);
		update.set("sellAmount", BigDecimal.ZERO);
		DailyAmount dailyAmount = zhongZhengMongoTemplate
				.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true),
						DailyAmount.class);

		logger.info("update or save  dailyAmount ：{}", dailyAmount);
	}

	/**
	 * to be refactored to remove duplicate code
	 */
	@Override
	public BigDecimal calcYieldValue(String userUuid, Long prodId, String startDate, String endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").gte(startDate).lte(endDate))
				.addCriteria(Criteria.where("userProdId").is(prodId));

		List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
		BigDecimal assetOfEndDay = BigDecimal.ZERO;
		BigDecimal assetOfStartDay = BigDecimal.ZERO;
		BigDecimal intervalAmount = BigDecimal.ZERO;
		for (DailyAmount dailyAmount : dailyAmountList) {
			if (dailyAmount.getDate().equals(startDate) && dailyAmount.getAsset() != null) {
				assetOfStartDay = assetOfStartDay.add(dailyAmount.getAsset());
			} else if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
				assetOfEndDay = assetOfEndDay.add(dailyAmount.getAsset());
			}

			if (dailyAmount.getBonus() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getBonus());
			}
			if (dailyAmount.getSellAmount() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
			}
			if (dailyAmount.getBuyAmount() != null) {
				intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
			}
		}

		return assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount);
	}

	@Override
	public PortfolioInfo calculateProductValue(String userUuid, Long userId, Long prodId,
			String startDate, String endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").gte(startDate).lte(endDate))
				.addCriteria(Criteria.where("userProdId").is(prodId));

		List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
		BigDecimal assetOfEndDay = BigDecimal.ZERO;
		BigDecimal intervalAmount = BigDecimal.ZERO;
		BigDecimal buyAmount = BigDecimal.ZERO;

		LocalDate endLocalDate = InstantDateUtil.format(endDate, "yyyyMMdd");
		String oneDayBefore = InstantDateUtil.format(endLocalDate.plusDays(-1), "yyyyMMdd");

		BigDecimal assetOfOneDayBefore = BigDecimal.ZERO;
		BigDecimal intervalAmountOfEndDay = BigDecimal.ZERO;

		for (DailyAmount dailyAmount : dailyAmountList) {
			//计算结束日总资产
			if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
				assetOfEndDay = assetOfEndDay
						.add(dailyAmount.getAsset().divide(new BigDecimal(100), MathContext.DECIMAL128));
			}
			//计算结束日前一天总资产
			if (dailyAmount.getDate().equals(oneDayBefore) && dailyAmount.getAsset() != null) {
				assetOfOneDayBefore = assetOfOneDayBefore
						.add(dailyAmount.getAsset().divide(new BigDecimal(1000), MathContext.DECIMAL128));
			}

			//期间分红 以及 结束日当日分红
			if (dailyAmount.getBonus() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getBonus());
				//结束日当天确认分红
				if (dailyAmount.getDate().equals(endDate)) {
					intervalAmountOfEndDay = intervalAmountOfEndDay.add(dailyAmount.getBonus());
				}
			}

			//期间赎回 以及结束日当天确认赎回
			if (dailyAmount.getSellAmount() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
				//借宿日当天赎回
				if (dailyAmount.getDate().equals(endDate)) {
					intervalAmountOfEndDay = intervalAmountOfEndDay.add(dailyAmount.getSellAmount());
				}
			}

			//期间申购 以及结束日当天确认申购
			if (dailyAmount.getBuyAmount() != null) {
				intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
				buyAmount = buyAmount.add(dailyAmount.getBuyAmount());

				if (dailyAmount.getDate().equals(endDate)) {
					intervalAmountOfEndDay = intervalAmountOfEndDay.subtract(dailyAmount.getBuyAmount());
				}
			}
		}

		//FIXME
		OrderResult orderResult = rpcOrderService
				.getOrderInfoByProdIdAndOrderStatus(prodId, TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

		//BigDecimal startAsset = BigDecimal.valueOf(orderResult.getPayAmount() / 100);
		BigDecimal startAsset = BigDecimal.ZERO;

		//累计收益 = 结束日总资产-开始日总资产 + 区间净赎回
		BigDecimal totalIncome = assetOfEndDay.add(intervalAmount).subtract(startAsset);

		//日收益=结束日净值 - 前一日净值 + 结束日的确认赎回+ 结束日的确认分红-结束日确认购买
		BigDecimal dailyIncome = assetOfEndDay.subtract(assetOfOneDayBefore)
				.add(intervalAmountOfEndDay);

		//日收益率= 结束日收益/（结束日净值-结束日收益）
		BigDecimal dailyIncomeRate = BigDecimal.ZERO;
		if (assetOfEndDay.subtract(dailyIncome).compareTo(BigDecimal.ZERO) > 0) {
			dailyIncomeRate = dailyIncome
					.divide(assetOfEndDay.subtract(dailyIncome), MathContext.DECIMAL128);
		}

		//区间收益率 =(区间结束总资产-起始总资产+分红+赎回-区间购买金额)/(起始总资产+区间购买金额)
		BigDecimal totalIncomeRate = BigDecimal.ZERO;
		if ((startAsset.add(buyAmount)).compareTo(BigDecimal.ZERO) > 0) {
			totalIncomeRate = assetOfEndDay.subtract(startAsset).add(intervalAmount)
					.divide(startAsset.add(buyAmount),
							MathContext.DECIMAL128);
		}

		PortfolioInfo portfolioInfo = new PortfolioInfo();
		portfolioInfo.setTotalAssets(assetOfEndDay.setScale(2, RoundingMode.HALF_UP));
		portfolioInfo.setTotalIncome(totalIncome.setScale(2, RoundingMode.HALF_UP));
		portfolioInfo.setTotalIncomeRate(totalIncomeRate.setScale(2, RoundingMode.HALF_UP));
		portfolioInfo.setDailyIncome(dailyIncome.setScale(2, RoundingMode.HALF_UP));
		portfolioInfo.setDailyIncomeRate(dailyIncomeRate.setScale(2, RoundingMode.HALF_UP));

		return portfolioInfo;
	}

	/**
	 * @param startDate yyyyMMdd
	 * @param endDate yyyyMMdd
	 */
	@Override
	public BigDecimal calcYieldRate(String userUuid, Long prodId, String startDate, String endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").gte(startDate).lte(endDate))
				.addCriteria(Criteria.where("userProdId").is(prodId));

		List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
		BigDecimal assetOfEndDay = BigDecimal.ZERO;
		BigDecimal assetOfStartDay = BigDecimal.ZERO;
		BigDecimal intervalAmount = BigDecimal.ZERO;
		BigDecimal buyAmount = BigDecimal.ZERO;
		for (DailyAmount dailyAmount : dailyAmountList) {
			if (dailyAmount.getDate().equals(startDate) && dailyAmount.getAsset() != null) {
				assetOfStartDay = assetOfStartDay.add(dailyAmount.getAsset());
			} else if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
				assetOfEndDay = assetOfEndDay.add(dailyAmount.getAsset());
			}

			if (dailyAmount.getBonus() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getBonus());
			}
			if (dailyAmount.getSellAmount() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
			}
			if (dailyAmount.getBuyAmount() != null) {
				intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
				buyAmount = buyAmount.add(dailyAmount.getBuyAmount());
			}
		}

		BigDecimal result = BigDecimal.ZERO;
		if (assetOfStartDay.compareTo(BigDecimal.ZERO) != 0) {
			//(区间结束总资产-起始总资产+分红+赎回-区间购买金额)/(起始总资产+区间购买金额)
			result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount)
					.divide(assetOfStartDay.add(buyAmount),
							MathContext.DECIMAL128);
		}

		return result;
	}

	/**
	 * @param startDate yyyyMMdd
	 * @param endDate yyyyMMdd
	 */
	@Override
	public BigDecimal calculateProductYieldRate(String userUuid, Long userId, Long prodId,
			String startDate,
			String endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").gte(startDate).lte(endDate))
				.addCriteria(Criteria.where("userProdId").is(prodId));

		List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
		BigDecimal assetOfEndDay = BigDecimal.ZERO;
		BigDecimal intervalAmount = BigDecimal.ZERO;
		BigDecimal buyAmount = BigDecimal.ZERO;
		for (DailyAmount dailyAmount : dailyAmountList) {
			if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
				assetOfEndDay = assetOfEndDay.add(dailyAmount.getAsset());
			}
			if (dailyAmount.getBonus() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getBonus());
			}
			if (dailyAmount.getSellAmount() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
			}
			if (dailyAmount.getBuyAmount() != null) {
				intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
				buyAmount = buyAmount.add(dailyAmount.getBuyAmount());
			}
		}

		BigDecimal result = BigDecimal.ZERO;

		OrderResult orderResult = rpcOrderService
				.getOrderInfoByProdIdAndOrderStatus(userId, TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

		BigDecimal assetOfStartDay = BigDecimal.valueOf(orderResult.getPayFee());

		if (assetOfStartDay.compareTo(BigDecimal.ZERO) != 0) {
			//(区间结束总资产-起始总资产+分红+赎回-区间购买金额)/(起始总资产+区间购买金额)
			result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount)
					.divide(assetOfStartDay.add(buyAmount),
							MathContext.DECIMAL128);
		}

		return result;
	}

	@Override
	public BigDecimal calcYieldValue(String userUuid, String startDate, String endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").gte(startDate).lte(endDate));

		List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
		BigDecimal assetOfEndDay = BigDecimal.ZERO;
		BigDecimal assetOfStartDay = BigDecimal.ZERO;
		BigDecimal intervalAmount = BigDecimal.ZERO;
		for (DailyAmount dailyAmount : dailyAmountList) {
			if (dailyAmount.getDate().equals(startDate) && dailyAmount.getAsset() != null) {
				assetOfStartDay = assetOfStartDay.add(dailyAmount.getAsset());
			} else if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
				assetOfEndDay = assetOfEndDay.add(dailyAmount.getAsset());
			}

			if (dailyAmount.getBonus() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getBonus());
			}
			if (dailyAmount.getSellAmount() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
			}
			if (dailyAmount.getBuyAmount() != null) {
				intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
			}
		}

		return assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount);
	}

	@Override
	public BigDecimal calcYieldRate(String userUuid, String startDate, String endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").gte(startDate).lte(endDate));

		List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
		BigDecimal assetOfEndDay = BigDecimal.ZERO;
		BigDecimal assetOfStartDay = BigDecimal.ZERO;
		BigDecimal intervalAmount = BigDecimal.ZERO;
		BigDecimal buyAmount = BigDecimal.ZERO;
		for (DailyAmount dailyAmount : dailyAmountList) {
			if (dailyAmount.getDate().equals(startDate) && dailyAmount.getAsset() != null) {
				assetOfStartDay = assetOfStartDay.add(dailyAmount.getAsset());
			} else if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
				assetOfEndDay = assetOfEndDay.add(dailyAmount.getAsset());
			}

			if (dailyAmount.getBonus() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getBonus());
			}
			if (dailyAmount.getSellAmount() != null) {
				intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
			}
			if (dailyAmount.getBuyAmount() != null) {
				intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
				buyAmount = buyAmount.add(dailyAmount.getBuyAmount());
			}
		}

		BigDecimal result = BigDecimal.ZERO;
		if (assetOfStartDay.compareTo(BigDecimal.ZERO) != 0) {
			//(区间结束总资产-起始总资产+分红+赎回-区间购买金额)/(起始总资产+区间购买金额)
			result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount)
					.divide((assetOfStartDay.add(buyAmount)), MathContext.DECIMAL128);
		}

		return result;
	}

	//    @Scheduled(cron = "0 0 3 * * ?", zone= "Asia/Shanghai")
//   定时任务改为脚本执行
	@Deprecated
	@Override
	public void dailyCalculation() throws Exception {
		logger.info("daily calculation every morning: {}", new Date());

		dailyCalculation(getYesterdayAsString());
	}

	@Override
	public void dailyCalculation(String date, List<UiUser> uiUsers) {
		for (UiUser user : uiUsers) {

			List<UiProducts> userProducts = uiProductRepo.findByUserId(user.getId());
			for (UiProducts prod : userProducts) {

				// 查询时间晚于购买时间
				LocalDate localDate = InstantDateUtil.format(date, "yyyyMMdd");
				localDate.plusDays(1);
				if (localDate.atTime(0, 0, 0).toInstant(ZoneOffset.UTC).toEpochMilli() < prod
						.getCreateDate()) {
					continue;
				}

				List<UiProductDetail> prodDetails = uiProductDetailRepo.findAllByUserProdId(prod.getId());
				for (UiProductDetail detail : prodDetails) {

					String fundCode = detail.getFundCode();
					initDailyAmount(user.getUuid(), prod.getProdId(), detail.getUserProdId(), date, fundCode);
					try {
						//计算当日总资产
						calcDailyAsset2(user.getUuid(), prod.getProdId(), detail.getUserProdId(), fundCode,
								date, detail);

						//获取当日分红，以及确认购买和赎回的金额
						calcIntervalAmount2(user.getUuid(), prod.getProdId(),
								detail.getUserProdId(), fundCode, date);
					} catch (Exception e) {
						logger.error("计算{用户:{},基金code:{},基金名称：{}}日收益出错", detail.getCreateBy(),
								detail.getFundCode(), detail.getFundName());
						logger.error(e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * FIXME date:2018-01-27 author: pierre  threadNum 表示线程数量而不是每个线程处理的数据量
	 */
	@Override
	public void dailyCalculation(String date) throws Exception {
		final List<UiUser> users = userInfoRepository.findAll();
		if (users.isEmpty()) {
			logger.info("user list is empty.");
			return;
		}
		int size = users.size();

		if (size == 1) {
			dailyCalculation(date, users);
		} else {
			int countDown;
			if (size % threadNum == 0) {
				countDown = size / threadNum;
			} else {
				countDown = size / threadNum + 1;
			}
			ExecutorService threadPool = Executors.newCachedThreadPool();
			CountDownLatch countDownLatch = new CountDownLatch(countDown);
			for (int i = 0; i < countDown; i++) {
				int fromIndex = i * threadNum;
				int toIndex = (i + 1) * threadNum <= size ? (i + 1) * threadNum : size;
				threadPool.submit(new FinanceProdCalculate(this, date,
						users.subList(fromIndex, toIndex), countDownLatch));
			}
			countDownLatch.await();
			logger.info("all thread process over.");
		}
	}

	@Override
	public BigDecimal getAssert(String userUuid, Long prodId) throws Exception {
		BigDecimal fundAsset = new BigDecimal(0);
		logger.info("dailyAmount:{}", fundAsset);
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("prodId").is(prodId));

		List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
		if (dailyAmountList != null && dailyAmountList.size() > 0) {
			fundAsset = dailyAmountList.get(0).getAsset();
		}
		return fundAsset;
	}


}
