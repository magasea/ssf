package com.shellshellfish.aaas.userinfo.service.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.BonusInfo;
import com.shellshellfish.aaas.userinfo.model.ConfirmResult;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.FundInfo;
import com.shellshellfish.aaas.userinfo.model.FundShare;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.CoinFundYieldRate;
import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import com.shellshellfish.aaas.userinfo.model.dao.FundYieldRate;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import org.springframework.data.mongodb.core.aggregation.Aggregation;
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

		Optional<Integer> fundQuantityOptional = Optional.ofNullable(uiProductDetail.getFundQuantity());
		BigDecimal share = new BigDecimal(fundQuantityOptional.orElse(0));
		share = share.divide(new BigDecimal(100));

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

		BigDecimal rateOfSellFund;

		//货币即基金赎回费率为零
		if (MonetaryFundEnum.containsCode(fundCode)) {
			rateOfSellFund = BigDecimal.ZERO;
		} else {
			rateOfSellFund = fundTradeApiService.getRate(fundCode, "024");
		}

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

	@Override
	public List<Map<String, Object>> getCalcYieldof7days(String fundCode, String type, String date)
			throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		if (!fundCode.contains("OF") && !fundCode.contains("SH") && !fundCode.contains("SZ")) {
			fundCode = fundCode + ".OF";
		}
		Date enddate = null;
		long sttime = 0L;
		long endtime = 0L;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String settingdate = "";
		if (date == null) {
			settingdate = sdf.format(new Date());
		} else {
			settingdate = date;
		}
		enddate = sdf.parse(settingdate);
		endtime = enddate.getTime() / 1000;

		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(enddate);
		if (type.equals("1")) {
			gc.add(2, -Integer.parseInt("3"));//3 month
		} else if (type.equals("2")) {
			gc.add(2, -Integer.parseInt("6"));//6 month
		} else if (type.equals("3")) {
			gc.add(2, -Integer.parseInt("12"));//1 year
		} else {
			gc.add(2, -Integer.parseInt("36"));//3 year
		}

		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime()));
		sttime = gc.getTime().getTime() / 1000;

		// 货币基金使用附权单位净值
		List<CoinFundYieldRate> coinFundYieldRateList = mongoCoinFundYieldRateRepository
				.findAllByCodeAndQueryDateIsBetween(fundCode, sttime, endtime,
						new Sort(new Order(Direction.DESC, "querydate")));
		if (coinFundYieldRateList != null && coinFundYieldRateList.size() > 0) {
			for (int i = 0; i < coinFundYieldRateList.size(); i++) {
				Map<String, Object> resultMap = new HashMap<String, Object>();
				CoinFundYieldRate coinFundYieldRate = coinFundYieldRateList.get(i);
				resultMap.put("profit", coinFundYieldRate.getYieldOf7Days());
				resultMap.put("yieldOf7Days", coinFundYieldRate.getYieldOf7Days());
				resultMap.put("code", coinFundYieldRate.getCode());
				Long queryDate = coinFundYieldRate.getQueryDate();
				LocalDateTime localDateTime = LocalDateTime
						.ofInstant(Instant.ofEpochSecond(queryDate), ZoneId.systemDefault());
				String dateNow = localDateTime.toLocalDate().toString();
				resultMap.put("querydate", dateNow);
				resultMap.put("date", dateNow);
				resultList.add(resultMap);
			}
		}
		return resultList;
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

		List<MongoUiTrdZZInfo> mongoUiTrdZZInfoBuy = mongoUiTrdZZInfoRepo
				.findByUserProdIdAndFundCodeAndTradeTypeAndTradeStatusAndConfirmDate(userProdId,
						fundCode, TrdOrderOpTypeEnum.BUY.getOperation(),
						TrdOrderStatusEnum.CONFIRMED.getStatus(), startDate);

		List<MongoUiTrdZZInfo> mongoUiTrdZZInfoSell = mongoUiTrdZZInfoRepo
				.findByUserProdIdAndFundCodeAndTradeTypeAndTradeStatusAndConfirmDate(userProdId,
						fundCode, TrdOrderOpTypeEnum.REDEEM.getOperation(),
						TrdOrderStatusEnum.CONFIRMED.getStatus(), startDate);

		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").is(startDate))
				.addCriteria(Criteria.where("fundCode").is(fundCode))
				.addCriteria(Criteria.where("prodId").is(prodId))
				.addCriteria(Criteria.where("userProdId").is(userProdId));

		Update update = new Update();

		BigDecimal buyAmount = BigDecimal.ZERO;
		BigDecimal sellAmount = BigDecimal.ZERO;

		for (MongoUiTrdZZInfo buy : mongoUiTrdZZInfoBuy) {
			buyAmount = buyAmount.add(Optional.ofNullable(buy)
					.map(m -> TradeUtil.getBigDecimalNumWithDiv100(m.getTradeTargetSum()))
					.orElse(BigDecimal.ZERO));
		}

		for (MongoUiTrdZZInfo sell : mongoUiTrdZZInfoSell) {
			sellAmount = sellAmount.add(Optional.ofNullable(sell)
					.map(m -> TradeUtil.getBigDecimalNumWithDiv100(m.getTradeTargetSum()))
					.orElse(BigDecimal.ZERO));
		}

		update.set("buyAmount", buyAmount);
		update.set("sellAmount", sellAmount);

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
	public PortfolioInfo calculateProductValue(String userUuid, Long prodId,
			String startDate, String endDate) {
		final String FORMAT_PATTERN = "yyyyMMdd";

		// 区间数据
		DailyAmountAggregation dailyAmountAggregation = aggregation(userUuid, startDate, endDate,
				prodId);

		if (dailyAmountAggregation == null) {
			return new PortfolioInfo();
		}
		//区间结束日前一天数据
		LocalDate startLocalDate = InstantDateUtil.format(startDate, FORMAT_PATTERN);
		LocalDate endLocalDate = InstantDateUtil.format(endDate, FORMAT_PATTERN);
		LocalDate oneDayBefore = endLocalDate.plusDays(-1);
		String oneDayBeforeStr = InstantDateUtil.format(oneDayBefore, FORMAT_PATTERN);

		//区间结束日数据
		DailyAmountAggregation dailyAmountAggregationOfEndDay = aggregation(userUuid, endDate, endDate,
				prodId);
		//结束日前一天数据
		DailyAmountAggregation dailyAmountAggregationOfOneDayBefore = aggregation(userUuid,
				oneDayBeforeStr, oneDayBeforeStr, prodId);

		if (dailyAmountAggregationOfEndDay == null) {

			LocalDate endLocalDateCopy;
			LocalDate oneDayBeforeCopy = oneDayBefore;

			while (dailyAmountAggregationOfEndDay == null && oneDayBeforeCopy.isAfter(startLocalDate)) {
				if (dailyAmountAggregationOfOneDayBefore != null) {
					//前推一天
					oneDayBeforeCopy = oneDayBeforeCopy.plusDays(-1);
					dailyAmountAggregationOfEndDay = dailyAmountAggregationOfOneDayBefore;
					dailyAmountAggregationOfOneDayBefore = aggregation(userUuid,
							InstantDateUtil.format(oneDayBeforeCopy, FORMAT_PATTERN),
							InstantDateUtil.format(oneDayBeforeCopy, FORMAT_PATTERN), prodId);
				} else {
					//前推两天
					endLocalDateCopy = oneDayBeforeCopy.plusDays(-1);
					oneDayBeforeCopy = endLocalDateCopy.plusDays(-1);

					dailyAmountAggregationOfEndDay = aggregation(userUuid,
							InstantDateUtil.format(endLocalDateCopy, FORMAT_PATTERN),
							InstantDateUtil.format(endLocalDateCopy, FORMAT_PATTERN), prodId);

					dailyAmountAggregationOfOneDayBefore = aggregation(userUuid,
							InstantDateUtil.format(oneDayBeforeCopy, FORMAT_PATTERN),
							InstantDateUtil.format(oneDayBeforeCopy, FORMAT_PATTERN), prodId);
				}
			}
		}

		if (dailyAmountAggregationOfEndDay == null) {
			return new PortfolioInfo();
		}

		//区间数据
		BigDecimal buyAmount = dailyAmountAggregation.getBuyAmount();
		BigDecimal sellAmount = dailyAmountAggregation.getSellAmount();
		BigDecimal bonus = dailyAmountAggregation.getBonus();
		// 区间净赎回金额= 区间该基金累计分红现金+区间该基金累计赎回金额-区间该基金累计购买金额
		BigDecimal intervalAmount = bonus.add(sellAmount).subtract(buyAmount);

		//区间结束日数据
		BigDecimal assetOfEndDay = dailyAmountAggregationOfEndDay.getAsset();
		BigDecimal buyAmountOfEndDay = dailyAmountAggregationOfEndDay.getBuyAmount();
		BigDecimal sellAmountOfEndDay = dailyAmountAggregationOfEndDay.getSellAmount();
		BigDecimal bonusOfEndDay = dailyAmountAggregationOfEndDay.getBonus();

		if (dailyAmountAggregationOfOneDayBefore == null) {
			dailyAmountAggregationOfOneDayBefore = DailyAmountAggregation.getEmptyInstance();
		}

		//区间结束日前一天数据
		Optional<DailyAmountAggregation> dailyAmountAggregationOfOneDayBeforeOptional = Optional
				.ofNullable(dailyAmountAggregationOfOneDayBefore);

		BigDecimal assetOfOneDayBefore = dailyAmountAggregationOfOneDayBeforeOptional
				.map(DailyAmountAggregation::getAsset).orElse(BigDecimal.ZERO);

		//区间开始总资产 恒为零
		BigDecimal startAsset = BigDecimal.ZERO;

		//累计收益 = 结束日总资产 - 开始日总资产 + 区间净赎回
		BigDecimal totalIncome = assetOfEndDay.add(intervalAmount).subtract(startAsset);

		//日收益=结束日净值 - 前一日净值
		BigDecimal dailyIncome = assetOfEndDay.subtract(assetOfOneDayBefore);

		BigDecimal totalIncomeRate = BigDecimal.ZERO;
		if (startAsset.add(buyAmount).compareTo(BigDecimal.ZERO) != 0) {
			//区间收益率 =(区间结束总资产-起始总资产+区间净赎回金额)/(起始总资产+区间购买金额)
			totalIncomeRate = assetOfEndDay.subtract(startAsset).add(intervalAmount)
					.divide(startAsset.add(buyAmount), MathContext.DECIMAL128);

		}
		PortfolioInfo portfolioInfo = new PortfolioInfo();

		portfolioInfo.setTotalAssets(assetOfEndDay.setScale(4, RoundingMode.HALF_UP));
		portfolioInfo.setTotalIncome(totalIncome.setScale(4, RoundingMode.HALF_UP));
		portfolioInfo.setTotalIncomeRate(totalIncomeRate.setScale(4, RoundingMode.HALF_UP));
		portfolioInfo.setDailyIncome(dailyIncome.setScale(4, RoundingMode.HALF_UP));

		//设置区间分红 ，申购和赎回
		portfolioInfo.setBonus(bonus);
		portfolioInfo.setBuyAmount(buyAmount);
		portfolioInfo.setSellAmount(sellAmount);

		//设置最后一日 分红，申购以及赎回
		portfolioInfo.setBonusOfEndDay(bonusOfEndDay);
		portfolioInfo.setBuyAmountOfEndDay(buyAmountOfEndDay);
		portfolioInfo.setSellAmountOfEndDay(sellAmountOfEndDay);

		portfolioInfo.setAssetOfOneDayBefore(assetOfOneDayBefore);
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
				if (InstantDateUtil.getEpochSecondOfZero(localDate) * 1000 < prod.getCreateDate()) {
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
								detail.getFundCode(), detail.getFundName(), e);
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


	private DailyAmountAggregation aggregation(String userUuid, String startDate, String endDate,
			Long prodId) {

		Aggregation agg = newAggregation(
				match(Criteria.where("userUuid").is(userUuid)),
				match(Criteria.where("date").gte(startDate).lte(endDate)),
				match(Criteria.where("userProdId").is(prodId)),
				group("userProdId")
						.sum("sellAmount").as("sellAmount")
						.sum("asset").as("asset")
						.sum("bonus").as("bonus")
						.sum("buyAmount").as("buyAmount")
		);
		return zhongZhengMongoTemplate
				.aggregate(agg, "dailyAmount", DailyAmountAggregation.class).getUniqueMappedResult();

	}
}
