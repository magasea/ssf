package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.model.BonusInfo;
import com.shellshellfish.aaas.userinfo.model.ConfirmResult;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.FundInfo;
import com.shellshellfish.aaas.userinfo.model.FundShare;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.service.FundTradeApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	@Qualifier("secondaryMongoTemplate")
	private MongoTemplate mongoTemplate;

	@Value("${daily-finance-calculate-thread:10}")
	private int threadNum;

	@Autowired
	private UserInfoRepository userInfoRepository;


	@Autowired
	UserInfoBankCardsRepository userInfoBankCardsRepository;

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
		DailyAmount dailyAmount = mongoTemplate
				.findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
		logger.info("dailyAmount:{}", dailyAmount);

		return fundAsset;
	}

	private BigDecimal calcDailyAsset2(String userUuid, Long prodId, Long userProdId, String fundCode,
			String date, UiProductDetail uiProductDetail) throws Exception {
		FundInfo fundInfo = fundTradeApiService.getFundInfoAsEntity(fundCode);
		BigDecimal share = new BigDecimal(uiProductDetail.getFundQuantity());

		if (BigDecimal.ZERO.equals(share)) {
			return BigDecimal.ZERO;
		}

		BigDecimal netValue = new BigDecimal(fundInfo.getPernetvalue());
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

		DailyAmount dailyAmount = mongoTemplate
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
			DailyAmount dailyAmount = mongoTemplate
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

			DailyAmount dailyAmount = mongoTemplate
					.findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
			logger.info("dailyAmount:{}", dailyAmount);
		}
	}


	private void calcIntervalAmount2(String openId, String userUuid, Long prodId, Long userProdId,
			String fundCode, String startDate) throws Exception {
		List<BonusInfo> bonusInfoList = fundTradeApiService.getBonusList(openId, fundCode, startDate);
		final String ZERO = "0.00";

		for (BonusInfo info : bonusInfoList) {
			if (info.getFactbonussum() == null || ZERO.equals(info.getFactbonussum())) {
				continue;
			}

			if(!startDate.equals(info.getConfirmdate()))
				continue;

			Query query = new Query();
			query.addCriteria(Criteria.where("userUuid").is(userUuid))
					.addCriteria(Criteria.where("date").is(startDate))
					.addCriteria(Criteria.where("fundCode").is(fundCode))
					.addCriteria(Criteria.where("prodId").is(prodId))
					.addCriteria(Criteria.where("userProdId").is(userProdId));

			Update update = new Update();
			update.set("bonus", info.getFactbonussum());
			DailyAmount dailyAmount = mongoTemplate
					.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(false),
							DailyAmount.class);
			logger.info(
					"set bonus ==> dailyAmount:{}", dailyAmount);
		}

		List<ConfirmResult> confirmList = fundTradeApiService
				.getConfirmResults(openId, fundCode, startDate);
		for (ConfirmResult result : confirmList) {

			if (result.getTradeconfirmsum() == null || ZERO.equals(result.getTradeconfirmsum())) {
				continue;
			}

			if (!startDate.equals(result.getConfirmdate())) {
				continue;
			}

			Query query = new Query();
			query.addCriteria(Criteria.where("userUuid").is(userUuid))
					.addCriteria(Criteria.where("date").is(startDate))
					.addCriteria(Criteria.where("fundCode").is(fundCode))
					.addCriteria(Criteria.where("prodId").is(prodId))
					.addCriteria(Criteria.where("userProdId").is(userProdId));

			Update update = new Update();
			if ("022".equals(result.getCallingcode())) {
				update.set("buyAmount", result.getTradeconfirmsum());
			} else if ("024".equals(result.getCallingcode())) {
				update.set("sellAmount", result.getTradeconfirmsum());
			}

			DailyAmount dailyAmount = mongoTemplate
					.findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(false),
							DailyAmount.class);
			logger.info(
					"set buyAmount or sell Amount ==> dailyAmount:{}", dailyAmount);
		}
	}

	@Override
	public void initDailyAmount(String userUuid, Long prodId, Long userProdId, String date,
			String fundCode) {
		DailyAmount dailyAmount = new DailyAmount();
		dailyAmount.setUserUuid(userUuid);
		dailyAmount.setDate(date);
		dailyAmount.setProdId(prodId);
		dailyAmount.setUserProdId(userProdId);
		dailyAmount.setFundCode(fundCode);
		dailyAmount.setAsset(BigDecimal.ZERO);
		dailyAmount.setBonus(BigDecimal.ZERO);
		dailyAmount.setBuyAmount(BigDecimal.ZERO);
		dailyAmount.setSellAmount(BigDecimal.ZERO);
		logger.info("insert dailyAmount ：{}", dailyAmount);
		mongoTemplate.save(dailyAmount);
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

		List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
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

		List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
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

		BigDecimal result = BigDecimal.ZERO;
		if (assetOfStartDay.compareTo(BigDecimal.ZERO) != 0) {
			result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount)
					.divide(assetOfStartDay, MathContext.DECIMAL128);
		}

		return result;
	}

	@Override
	public BigDecimal calcYieldValue(String userUuid, String startDate, String endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(userUuid))
				.addCriteria(Criteria.where("date").gte(startDate).lte(endDate));

		List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
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

		List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
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

		BigDecimal result = BigDecimal.ZERO;
		if (assetOfStartDay.compareTo(BigDecimal.ZERO) != 0) {
			result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount)
					.divide(assetOfStartDay, MathContext.DECIMAL128);
		}

		return result;
	}

	//    @Scheduled(cron = "0 0 3 * * ?", zone= "Asia/Shanghai")
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
				List<UiProductDetail> prodDetails = uiProductDetailRepo.findAllByUserProdId(prod.getId());
				for (UiProductDetail detail : prodDetails) {
					String fundCode = detail.getFundCode();
					initDailyAmount(user.getUuid(), prod.getProdId(), detail.getUserProdId(), date, fundCode);
					try {
						calcDailyAsset2(user.getUuid(), prod.getProdId(), detail.getUserProdId(), fundCode,
								date, detail);
						calcIntervalAmount2(getZZOpenId(user.getId()), user.getUuid(), prod.getProdId(),
								detail.getUserProdId(), fundCode, date);
					} catch (Exception e) {
						logger.error("计算{用户:{},基金code:{},基金名称：{}}日收益出错", detail.getCreateBy(),
								detail.getFundCode(), detail.getFundName());
						logger.error(e.getMessage());
						break;
					}
				}
			}
		}
	}

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

		List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
		if (dailyAmountList != null && dailyAmountList.size() > 0) {
			fundAsset = dailyAmountList.get(0).getAsset();
		}
		return fundAsset;
	}

	/**
	 * FIXME : 时间待定  标记人：pierre 一个用户可以绑定多张银行卡的情况下，这里是有BUG的， 因为大部分情况下用户只会绑定自己的身份证，权宜之计，我们选取第一个可用的身份证号，来生成中证openId
	 */
	private String getZZOpenId(Long userId) {
		List<UiBankcard> bankcards = userInfoBankCardsRepository.findAllByUserIdAndStatusIs(userId, 1);
		if (CollectionUtils.isEmpty(bankcards)) {
			return null;
		}
		for (int i = 0; i < bankcards.size(); i++) {
			if (bankcards.get(i) != null && bankcards.get(i).getUserPid() != null) {
				return TradeUtil.getZZOpenId(bankcards.get(i).getUserPid());
			}
		}

		return null;
	}
}
