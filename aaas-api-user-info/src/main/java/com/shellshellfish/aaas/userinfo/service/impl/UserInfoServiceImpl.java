package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.BankCardStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.pay.ZhongZhengQueryByOrderDetailId;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.MongoUiTrdLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UiProductDetailDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBriefDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoCompanyInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolioDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.service.RpcOrderService;
import com.shellshellfish.aaas.userinfo.service.UiProductService;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import com.shellshellfish.aaas.userinfo.utils.DateUtil;
import io.grpc.ManagedChannel;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class UserInfoServiceImpl implements UserInfoService {

	Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

	@Autowired
	UserInfoRepoService userInfoRepoService;

	@Autowired
	UserFinanceProdCalcService userFinanceProdCalcService;

	@Autowired
	UiProductService uiProductService;


	@Autowired
	MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;


	@Autowired
	RpcOrderService rpcOrderService;

	@Autowired
	@Qualifier("zhongZhengMongoTemplate")
	private MongoTemplate mongoTemplate;

	PayRpcServiceFutureStub payRpcServiceFutureStub;

	@Autowired
	ManagedChannel managedPayChannel;

	@PostConstruct
	void init() {
		payRpcServiceFutureStub = PayRpcServiceGrpc.newFutureStub(managedPayChannel);
	}

	@Override
	public UserBaseInfoDTO getUserInfoBase(String userUuid) throws Exception {
		Long userId = getUserIdFromUUID(userUuid);
		UserBaseInfoDTO userInfoDao = userInfoRepoService.getUserInfoBase(userId);
		// UserBaseInfo userBaseInfo = new UserBaseInfo();
		// if( null != userInfoDao) {
		// BeanUtils.copyProperties(userInfoDao, userBaseInfo);
		// }
		return userInfoDao;
	}

	@Override
	public UserInfoAssectsBriefDTO getUserInfoAssectsBrief(String userUuid) throws Exception {
		Long userId = getUserIdFromUUID(userUuid);
		// UserInfoAssectsBrief userInfoAssectsBrief = new
		// UserInfoAssectsBrief();
		UserInfoAssectsBriefDTO userInfoAssect = userInfoRepoService.getUserInfoAssectsBrief(userId);
		// if(null != userInfoAssect){
		// BeanUtils.copyProperties(userInfoAssect, userInfoAssectsBrief);
		// }
		return userInfoAssect;
	}

	@Override
	public List<BankCardDTO> getUserInfoBankCards(String userUuid) {
		Long userId = null;
		try {
			userId = getUserIdFromUUID(userUuid);
		} catch (Exception e) {
			throw new UserInfoException("404", "该用户不存在");
		}
		List<BankCardDTO> bankcards = null;
		try {
			bankcards = userInfoRepoService.getUserInfoBankCards(userId);
		} catch (Exception e) {
			throw new UserInfoException("404", "该用户暂时没有绑定银行卡");
		}
		// List<BankCard> bankCardsDto = new ArrayList<>();
		// for(UiBankcard uiBankcard: uiBankcards ){
		// BankCard bankCard = new BankCard();
		// BeanUtils.copyProperties(uiBankcard, bankCard);
		// bankCardsDto.add(bankCard);
		// }
		return bankcards;
	}

	@Override
	public List<UserPortfolioDTO> getUserPortfolios(String userUuid) throws Exception {
		Long userId = getUserIdFromUUID(userUuid);
		List<UserPortfolioDTO> userPortfolioDaos = userInfoRepoService.getUserPortfolios(userId);
		// List<UserPortfolio> userPortfolios = new ArrayList<>();
		// for(UiPortfolio userPortfolioDao: userPortfolioDaos){
		// UserPortfolio userPortfolio = new UserPortfolio();
		// BeanUtils.copyProperties(userPortfolioDao, userPortfolio);
		// userPortfolios.add(userPortfolio);
		// }
		return userPortfolioDaos;
	}

	@Override
	public BankCardDTO getUserInfoBankCard(String cardNumber) throws RuntimeException {
		BankCardDTO bankCard = userInfoRepoService.getUserInfoBankCard(cardNumber);
		// BankCard bankCard = new BankCard();
		// BeanUtils.copyProperties(uiBankcard, bankCard);
		return bankCard;
	}

	@Override
	public BankCardDTO createBankcard(Map params) throws Exception {
		Long userId = getUserIdFromUUID(params.get("userUuid").toString());
		UiBankcard uiBankcard = new UiBankcard();
		uiBankcard.setCardNumber(params.get("cardNumber").toString());
		uiBankcard.setUserName(params.get("cardUserName").toString());
		uiBankcard.setCellphone(params.get("cardCellphone").toString());
		uiBankcard.setUserPid(params.get("cardUserPid").toString());
		uiBankcard.setUserId(userId);
		uiBankcard.setStatus(BankCardStatusEnum.VALID.getStatus());
		if (!StringUtils.isEmpty(params.get("cardNumber"))) {
			String bankName = BankUtil.getNameOfBank(params.get("cardNumber").toString());
			if (!StringUtils.isEmpty(bankName)) {
				uiBankcard.setBankName(BankUtil.getNameOfBank(params.get("cardNumber").toString()));
			}
		}
		BankCardDTO bankCard = userInfoRepoService.addUserBankcard(uiBankcard);
		return bankCard;
	}

	@Override
	public List<AssetDailyReptDTO> getAssetDailyRept(String userUuid, Long beginDate, Long endDate)
			throws Exception {
		Long userId = getUserIdFromUUID(userUuid);
		List<AssetDailyReptDTO> uiAssetDailyRepts = userInfoRepoService
				.getAssetDailyRept(userId, beginDate, endDate);
		// List<AssetDailyRept> assetDailyRepts = new ArrayList<>();
		// for(UiAssetDailyRept uiAssetDailyRept: uiAssetDailyRepts){
		// AssetDailyRept assetDailyRept = new AssetDailyRept();
		// BeanUtils.copyProperties(uiAssetDailyRept, assetDailyRept);
		// assetDailyRept.setDate(new Date(uiAssetDailyRept.getDate()));
		// assetDailyRepts.add(assetDailyRept);
		// }
		return uiAssetDailyRepts;
	}

	@Override
	public AssetDailyReptDTO addAssetDailyRept(AssetDailyReptDTO assetDailyRept) {
		UiAssetDailyRept uiAssetDailyRept = new UiAssetDailyRept();
		BeanUtils.copyProperties(assetDailyRept, uiAssetDailyRept);
		uiAssetDailyRept.setDate(assetDailyRept.getDate().getTime());
		AssetDailyReptDTO result = userInfoRepoService.addAssetDailyRept(uiAssetDailyRept);
		// AssetDailyRept assetDailyReptResult = new AssetDailyRept();
		// BeanUtils.copyProperties(result, assetDailyReptResult);
		// Date date = new Date(result.getDate());
		// assetDailyRept.setDate(date);
		return result;
	}

	@Override
	public List<UserSysMsgDTO> getUserSysMsg(String userUuid)
			throws IllegalAccessException, InstantiationException {
		List<UserSysMsgDTO> userSysMsgs = userInfoRepoService.getUiSysMsg();
		// List<UserSysMsg> userSysMsgs = new ArrayList<>();
		// for(UiSysMsg uiSysMsg: uiSysMsgs){
		// UserSysMsg userSysMsg = new UserSysMsg();
		// BeanUtils.copyProperties(uiSysMsg, userSysMsg);
		// userSysMsgs.add(userSysMsg);
		// }
		return userSysMsgs;
	}

	@Override
	public List<UserPersonMsgDTO> getUserPersonMsg(String userUuid) throws Exception {
		Long userId = getUserIdFromUUID(userUuid);
		List<UserPersonMsgDTO> uiPersonMsgs = userInfoRepoService.getUiPersonMsg(userId);
		// List<UserPersonMsg> userPersonMsgs = new ArrayList<>();
		// for(UiPersonMsg uiPersonMsg: uiPersonMsgs){
		// UserPersonMsg userPersonMsg = new UserPersonMsg();
		// BeanUtils.copyProperties(uiPersonMsg, userPersonMsg);
		// userPersonMsgs.add(userPersonMsg);
		// }
		return uiPersonMsgs;
	}

	@Override
	public Boolean updateUserPersonMsg(String msgId, String userUuid, Boolean readedStatus)
			throws Exception {
		Long userId = getUserIdFromUUID(userUuid);
		Boolean result = userInfoRepoService.updateUiUserPersonMsg(msgId, userId, readedStatus);

		return result;
	}

	@Override
	public Page<TradeLogDTO> findByUserId(String userUuid, Pageable pageable) throws Exception {
		Long userId = getUserIdFromUUID(userUuid);
		Page<UiTrdLog> tradeLogsPage = userInfoRepoService.findTradeLogDtoByUserId(pageable, userId);
		Page<TradeLogDTO> tradeLogResult = MyBeanUtils
				.convertPageDTO(pageable, tradeLogsPage, TradeLogDTO.class);
		return tradeLogResult;
	}

	@Override
	public List<UserInfoFriendRuleDTO> getUserInfoFriendRules(Long bankId)
			throws InstantiationException, IllegalAccessException, RuntimeException {
		List<UserInfoFriendRuleDTO> userInfoFriendRules = userInfoRepoService.getUiFriendRule(bankId);
		return userInfoFriendRules;
	}

	@Override
	public UserInfoCompanyInfoDTO getCompanyInfo(String userUuid, Long bankId) {
		Long id = getCompanyId(userUuid, bankId);
		UiCompanyInfo uiCompanyInfo = userInfoRepoService.getCompanyInfo(id);
		UserInfoCompanyInfoDTO userInfoCompanyInfo = new UserInfoCompanyInfoDTO();
		if (null == uiCompanyInfo) {
			return userInfoCompanyInfo;
		}
		BeanUtils.copyProperties(uiCompanyInfo, userInfoCompanyInfo);
		return userInfoCompanyInfo;

	}

	// TODO: this function will be adjusted by business rule
	private Long getCompanyId(String userUuid, Long bankId) {
		return 1L;
	}

	private Long getUserIdFromUUID(String userUuid) throws Exception {
		Long userId = userInfoRepoService.getUserIdFromUUID(userUuid);
		return userId;
	}

	@Override
	public Boolean deleteBankCard(String userUuid, String bankCardNumber) {
		Boolean result = userInfoRepoService.deleteBankCard(userUuid, bankCardNumber);
		return result;
	}

	@Override
	public Boolean addUiUser(String userUuid, String cellphone, String isTestFlag) {
		Boolean result = userInfoRepoService.saveUser(userUuid, cellphone, isTestFlag);
		return result;
	}

	@Override
	public Boolean updateUiUser(String cellphone, String isTestFlag, String riskLevel) {
		Boolean result = userInfoRepoService.updateCellphone(cellphone, isTestFlag, riskLevel);
		return result;
	}

	@Override
	public UserBaseInfoDTO selectUiUser(String cellphone) {
		UserBaseInfoDTO result = userInfoRepoService.findByCellphone(cellphone);
		return result;
	}

	@Override
	public List<TradeLogDTO> findByUserId(String uuid) throws Exception {
		Long userId = getUserIdFromUUID(uuid);
		List<TradeLogDTO> uiTrdLogList = userInfoRepoService.findTradeLogDtoByUserId(userId);
		return uiTrdLogList;
	}

	@Override
	public List<ProductsDTO> findProductInfos(String uuid) throws Exception {
		List<ProductsDTO> products = userInfoRepoService.findTradeLogDtoByUserId(uuid);
		return products;
	}

	@Override
	public ProductsDTO findByProdId(String prodId)
			throws IllegalAccessException, InstantiationException {
		ProductsDTO products = userInfoRepoService.findByProdId(prodId);
		return products;
	}

	@Override
	public ApplyResult queryTrdResultByOrderDetailId(Long userId, Long orderDetailId) {
		ZhongZhengQueryByOrderDetailId.Builder requestBuilder = ZhongZhengQueryByOrderDetailId
				.newBuilder();

		requestBuilder.setOrderDetailId(orderDetailId);
		requestBuilder.setUserId(userId);

		try {
			requestBuilder.setUserId(userId);
			requestBuilder.setOrderDetailId(orderDetailId);
			com.shellshellfish.aaas.finance.trade.pay.ApplyResult result = payRpcServiceFutureStub
					.queryZhongzhengTradeInfoByOrderDetailId(requestBuilder.build()).get();
			ApplyResult applyResult = new ApplyResult();
			BeanUtils.copyProperties(result, applyResult);
			return applyResult;
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return null;
		}
		return null;
	}

	@Override
	public Map<String, Object> getTrendYield(String userUuid) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> trendYieldList = new ArrayList<Map<String, Object>>();
		int days = 6;
		//遍历赋值
		for (int i = 0; i < days; i++) {
			Map trendYieldMap = new HashMap<>();
			String selectDate = DateUtil.getSystemDatesAgo(-i);
			String dayBeforeSelectDate = DateUtil.getSystemDatesAgo(-i - 1);
			trendYieldMap.put("date", selectDate);
			//调用对应的service
			BigDecimal rate = userFinanceProdCalcService
					.calcYieldValue(userUuid, dayBeforeSelectDate, selectDate);
			if (rate != null) {
				trendYieldMap.put("value", (rate.divide(new BigDecimal("100"), MathContext.DECIMAL128))
						.setScale(2, BigDecimal.ROUND_HALF_UP));
			} else {
				trendYieldMap.put("value", 0);
			}
			trendYieldList.add(trendYieldMap);
		}
		resultMap.put("trendYield", trendYieldList);
		return resultMap;
	}

	@Override
	public Map<String, Object> getTotalAssets(String uuid) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> productsList = this.getMyCombinations(uuid);
		if (productsList == null || productsList.size() == 0) {
			logger.error("我的智投组合暂时不存在");
			return new HashMap<String, Object>();
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		BigDecimal asserts = new BigDecimal(0);
		BigDecimal dailyIncome = new BigDecimal(0);
		BigDecimal totalIncome = new BigDecimal(0);
		if (productsList != null && productsList.size() > 0) {
			for (int i = 0; i < productsList.size(); i++) {
				Map<String, Object> products = productsList.get(i);
				if (products.get("totalAssets") != null) {
					asserts = asserts.add(new BigDecimal(products.get("totalAssets") + "")).setScale(2,
							RoundingMode.HALF_UP);
				}
				if (products.get("dailyIncome") != null) {
					dailyIncome = dailyIncome.add(new BigDecimal(products.get("dailyIncome") + "")).setScale(2,
							RoundingMode.HALF_UP);
				}
				if (products.get("totalIncome") != null) {
					totalIncome = totalIncome.add(new BigDecimal(products.get("totalIncome") + "")).setScale(2,
							RoundingMode.HALF_UP);
				}
			}
		}
		resultMap.put("assert", asserts.setScale(2, RoundingMode.HALF_UP));
		resultMap.put("dailyIncome", dailyIncome.setScale(2, BigDecimal.ROUND_HALF_UP));
		resultMap.put("totalIncome", totalIncome.setScale(2, RoundingMode.HALF_UP));
		if (asserts != BigDecimal.ZERO && !"0.00".equals(asserts + "")) {
			BigDecimal incomeRate = (totalIncome.divide(asserts, MathContext.DECIMAL128)).setScale(4,
					BigDecimal.ROUND_HALF_UP);
//			BigDecimal incomeRate = totalIncome.divide(asserts, 2, BigDecimal.ROUND_HALF_UP);
			resultMap.put("totalIncomeRate", incomeRate);
		} else {
			resultMap.put("totalIncomeRate", "0");
		}
		return resultMap;
	}


	/**
	 * 计算组合的累计净值，累计收益，累计收益率 ，日收益，日收益率
	 */
	@Override
	public PortfolioInfo getChicombinationAssets(String uuid, Long userId, ProductsDTO products) {

		List<OrderDetail> orderDetailPayWaitConfirm = rpcOrderService
				.getOrderDetails(products.getId(),
						TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

		//完全确认标志
		boolean flag = false;
		if (CollectionUtils.isEmpty(orderDetailPayWaitConfirm)) {
			flag = true;
		}

		Long startDate = products.getCreateDate();
		LocalDate startLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate),
				ZoneOffset.UTC).toLocalDate();
		String startDay = InstantDateUtil.format(startLocalDate, "yyyyMMdd");

		String endDay = InstantDateUtil.format(LocalDate.now().plusDays(-1), "yyyyMMdd");

		if (flag) {
			//完全确认
			products.setStatus(TrdOrderStatusEnum.CONFIRMED.getStatus());
			return userFinanceProdCalcService
					.calculateProductValue(uuid, products.getId(), startDay, endDay);
		} else {
			//部分确认
			logger.info("\n未完全确认数据 userProdId :{}\n", products.getId());
			products.setStatus(TrdOrderStatusEnum.PARTIALCONFIRMED.getStatus());
			return getPartConfirmFundInfo(uuid, userId, products.getId(), startDay, endDay);
		}


	}


	/**
	 * 计算组合部分确认的资产和收益
	 */
	private PortfolioInfo getPartConfirmFundInfo(String uuid, Long userId, Long prodId,
			String startDay, String endDay) {
		PortfolioInfo portfolioInfo = userFinanceProdCalcService
				.calculateProductValue(uuid, prodId, startDay, endDay);

		List<MongoUiTrdZZInfo> mongoUiTrdZZinfoList = mongoUiTrdZZInfoRepo
				.findAllByUserIdAndUserProdIdAndOperationsAndTradeStatus(userId, prodId,
						TrdOrderOpTypeEnum.BUY.getOperation(),
						TrdOrderStatusEnum.CONFIRMED.getStatus());

		//已经确认部分金额
		BigDecimal conifrmAsset = BigDecimal.ZERO;
		BigDecimal confirmAssetOfEndDay = BigDecimal.ZERO;
		for (MongoUiTrdZZInfo mongoUiTrdZZinfo : mongoUiTrdZZinfoList) {
			conifrmAsset.add(mongoUiTrdZZinfo == null ? BigDecimal.ZERO
					: TradeUtil.getBigDecimalNumWithDiv100(mongoUiTrdZZinfo.getTradeTargetSum()));

			if (endDay.equals(mongoUiTrdZZinfo.getConfirmDate())) {
				confirmAssetOfEndDay
						.add(TradeUtil.getBigDecimalNumWithDiv100(mongoUiTrdZZinfo.getTradeTargetSum()));
			}
		}

		OrderResult orderResult = rpcOrderService
				.getOrderInfoByProdIdAndOrderStatus(prodId, TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

		logger.info(
				"=======================================================================================");
		logger.info("\n\n{}\n\n", orderResult.getPayAmount());
		logger.info(
				"=======================================================================================");

		BigDecimal applyAsset = BigDecimal.valueOf(orderResult.getPayAmount())
				.divide(BigDecimal.valueOf(100));

		logger.info("\nuserProdId:{}  ===  applyAsset {}\n", prodId, applyAsset);
		BigDecimal assetOfEndDay = Optional.ofNullable(portfolioInfo.getTotalAssets())
				.orElse(BigDecimal.ZERO);

		logger.info("\nuserProdId:{}  === assetOfEndDay {}\n", prodId, assetOfEndDay);
		// 总资产 = 确认基金资产+ 未确认的基金的申购金额  = 结束日资产（即申购成功部分结束日资产） +（总申购资产-确认部分申购资产）
		BigDecimal asset = assetOfEndDay.add(applyAsset.subtract(conifrmAsset));

		logger.info("\nuserProdId:{}  === asset {}\n", prodId, asset);

		logger.info("\nuserProdId:{}  === confirmAsset {}\n", prodId, conifrmAsset);
		// 累计收益=确认部分资产- 确认部分申购金额  (默认未完全确认  不能追加和赎回)
		BigDecimal toltalIncome = assetOfEndDay.subtract(conifrmAsset);

		// 累计收益率= 累计收益/申购金额
		BigDecimal toltalIncomeRate = Optional.ofNullable(portfolioInfo.getTotalIncomeRate())
				.orElse(BigDecimal.ZERO);

		if (applyAsset.compareTo(BigDecimal.ZERO) != 0) {
			toltalIncomeRate = toltalIncome.divide(applyAsset, 4, RoundingMode.HALF_UP);
		}

		portfolioInfo.setTotalAssets(asset.setScale(4, RoundingMode.HALF_UP));
		portfolioInfo.setTotalIncome(toltalIncome.setScale(4, RoundingMode.HALF_UP));
		portfolioInfo.setTotalIncomeRate(toltalIncomeRate.setScale(4, RoundingMode.HALF_UP));

		return portfolioInfo;
	}


	@Override
	public List<Map<String, Object>> getTradeLogStatus(String uuid, Long userProdId)
			throws Exception {
		Long userId = getUserIdFromUUID(uuid);
		List<MongoUiTrdLogDTO> trdLogList = userInfoRepoService
				.findByUserIdAndProdId(userId, userProdId);
		Map<String, Map<String, Object>> resultMap = new HashMap<String, Map<String, Object>>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		if (trdLogList != null && trdLogList.size() > 0) {
			for (int i = 0; i < trdLogList.size(); i++) {
				Map<String, Object> resultMap2 = new HashMap<String, Object>();
				MongoUiTrdLogDTO trdLog = trdLogList.get(i);
				int status = trdLog.getTradeStatus();
				String lastModifiedDate = "0";
				if (trdLog.getLastModifiedDate() != 0) {
					lastModifiedDate = trdLog.getLastModifiedDate() + "";
				}
				if (resultMap.containsKey("A" + status)) {
					resultMap2 = resultMap.get("A" + status);
					if (Long.parseLong(resultMap2.get("lastModified") + "") < Long
							.parseLong(lastModifiedDate)) {
						resultMap2.put("lastModified", lastModifiedDate);
						resultMap2.put("time", DateUtil.getDateType(trdLog.getLastModifiedDate()));
						resultMap2.put("status", status + "");
						resultMap.put("A" + status, resultMap2);
					}
				} else {
					resultMap2.put("lastModified", lastModifiedDate);
					resultMap2.put("time", DateUtil.getDateType(trdLog.getLastModifiedDate()));
					resultMap2.put("status", status + "");
					resultMap.put("A" + status, resultMap2);
				}
			}
			for (Map map : resultMap.values()) {
				map.remove("lastModified");
				if (map.get("time") != null) {
					String dateTime = (String) map.get("time");
					String date[] = dateTime.split(" ");
					if (date.length == 2) {
						map.put("date", date[0]);
						map.put("time", date[1]);
					} else if (date.length == 1) {
						map.put("date", date[0]);
						map.remove("time");
					}
				}
				if (map.get("status") != null) {
					TrdOrderStatusEnum trdOrderStatusEnum[] = TrdOrderStatusEnum.values();
					String status = (String) map.get("status");
					for (TrdOrderStatusEnum temp : trdOrderStatusEnum) {
						if (status.equals(temp.getStatus() + "")) {
							map.put("status", temp.getComment());
							break;
						} else {
							map.put("status", "");
						}
					}
				}
				result.add(map);
			}
		}
		return result;
	}

	@Override
	public List<MongoUiTrdLogDTO> getTradeLogs(String uuid) throws Exception {
		Long userId = getUserIdFromUUID(uuid);
		List<MongoUiTrdLogDTO> trdLogList = userInfoRepoService.findByUserId(userId);
		return trdLogList;
	}

	@Override
	public List<Map<String, Object>> getMyCombinations(String uuid) throws Exception {
		List<Map<String, Object>> resultList = new ArrayList();
		List<ProductsDTO> productsList = this.findProductInfos(uuid);
		if (productsList == null || productsList.size() == 0) {
			logger.error("我的智投组合暂时不存在");
			return resultList;
		}

		Map<String, Object> resultMap;
		ProductsDTO products;
		for (int i = 0; i < productsList.size(); i++) {
			products = productsList.get(i);
			resultMap = new HashMap<String, Object>();
			resultMap.put("groupId", products.getProdId());
			resultMap.put("subGroupId", products.getGroupId());

			resultMap.put("title", products.getProdName());
			resultMap.put("createDate", products.getCreateDate());
			// 状态(0-待确认 1-已确认 -1-交易失败)
			List<UiProductDetailDTO> productDetailsList = uiProductService
					.getProductDetailsByProdId(products.getId());
			Integer count = 0;
			Integer fails = 0;
			Integer statusIsNull = 0;

			if (productDetailsList != null && productDetailsList.size() > 0) {
				for (int j = 0; j < productDetailsList.size(); j++) {
					UiProductDetailDTO uiProductDetailDTO = productDetailsList.get(j);
					if (uiProductDetailDTO.getStatus() != null) {
						if (uiProductDetailDTO.getStatus() != TrdOrderStatusEnum.CONFIRMED.getStatus()
								&& uiProductDetailDTO.getStatus() != TrdOrderStatusEnum.FAILED.getStatus()
								&& uiProductDetailDTO.getStatus() != TrdOrderStatusEnum.CANCEL.getStatus()) {
							count++;
						} else if (uiProductDetailDTO.getStatus() == TrdOrderStatusEnum.FAILED.getStatus()) {
							fails++;
						}
					} else {
						statusIsNull++;
					}
				}
				if (fails > 0) {
					if (fails == productDetailsList.size()) {
						//若组合中全部失败，则不显示
						continue;
					}
				}
				if (statusIsNull > 0) {
					if (statusIsNull == productDetailsList.size()) {
						//若组合中状态全部为NULL，则不显示
						continue;
					}
				}
				resultMap.put("count", count);
				if (count > 0) {
					resultMap.put("title2", "* 您有" + count + "支基金正在确认中");
				}
			}

			Long userId = getUserIdFromUUID(uuid);
			// 总资产
			PortfolioInfo portfolioInfo = this.getChicombinationAssets(uuid, userId, products);
			resultMap
					.put("totalAssets",
							Optional.ofNullable(portfolioInfo.getTotalAssets()).orElse(BigDecimal.ZERO)
									.setScale(2, RoundingMode.HALF_UP));
			// 日收益
			resultMap
					.put("dailyIncome",
							Optional.ofNullable(portfolioInfo.getDailyIncome()).orElse(BigDecimal.ZERO)
									.setScale(2, RoundingMode.HALF_UP));
			// 累计收益率
			resultMap.put("totalIncomeRate",
					Optional.ofNullable(portfolioInfo.getTotalIncomeRate()).orElse(BigDecimal.ZERO)
							.setScale(4, RoundingMode.HALF_UP));
			// 累计收益
			resultMap
					.put("totalIncome",
							Optional.ofNullable(portfolioInfo.getTotalIncome()).orElse(BigDecimal.ZERO)
									.setScale(2, RoundingMode.HALF_UP));

			// 智投组合产品ID
			resultMap.put("prodId", products.getId());
			// 买入日期
			resultMap.put("updateDate", DateUtil.getDateType(products.getUpdateDate()));

			resultList.add(resultMap);
		}
		return resultList;
	}

	@Override
	public Integer getUserRishLevel(String userId) {
		UiUser uiUser = userInfoRepoService.getUserInfoByUserUUID(userId);
		Optional<UiUser> userOptional = Optional.ofNullable(uiUser);
		return userOptional.map(m -> m.getRiskLevel()).orElse(-1);
	}

	/**
	 * 从DailyAmount获取，组合资产、组合日收益、组合起始日期
	 */
	public Map<String, Object> getCombinations(String uuid, Long prodId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(uuid))
				.addCriteria(Criteria.where("userProdId").is(prodId));
		query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC, "date"));
		List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
		String date = "";
		BigDecimal assets = BigDecimal.ZERO;
		BigDecimal assetsOneDayBeofre = BigDecimal.ZERO;
		int count = 0;
		if (dailyAmountList != null && dailyAmountList.size() > 0) {
			for (int i = 0; i < dailyAmountList.size(); i++) {
				DailyAmount dailyAmount = dailyAmountList.get(i);
				if (date.equals(dailyAmount.getDate()) || i == 0) {
					if (count == 0) {
						assets = assets.add(dailyAmount.getAsset());
					} else if (count == 1) {
						assetsOneDayBeofre = assetsOneDayBeofre.add(dailyAmount.getAsset());
					}
				} else {
					if (count == 1) {
						if (assetsOneDayBeofre != null && !assetsOneDayBeofre.equals(BigDecimal.ZERO)) {
							// 前一天的日期
							resultMap.put("date2", date);
							break;
						}
					} else if (count == 0) {
						resultMap.put("date", date);
						assetsOneDayBeofre = dailyAmount.getAsset();
					}
					count++;
				}
				date = dailyAmount.getDate();
			}
		}
		if (assets != null) {
			assets = (assets.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
			resultMap.put("assert", assets);
		} else {
			resultMap.put("assert", 0);
		}

		if (assetsOneDayBeofre != null) {
			assetsOneDayBeofre = (assetsOneDayBeofre.divide(new BigDecimal(100)))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			resultMap.put("dailyIncome", assets.subtract(assetsOneDayBeofre));
		} else {
			resultMap.put("dailyIncome", 0);
		}

		return resultMap;
	}


	@Override
	public Map<String, Object> getProducts(Long prodId)
			throws IllegalAccessException, InstantiationException {
		Map<String, Object> result = new HashMap<String, Object>();
		ProductsDTO product = new ProductsDTO();
		if (prodId != null) {
			product = userInfoRepoService.findByProdId(prodId + "");
			result.put("id", product.getId());
			result.put("userId", product.getUserId());
			result.put("groupId", product.getProdId());
			result.put("subGroupId", product.getGroupId());
			result.put("prodName", product.getProdName());
			result.put("status", product.getStatus());
		}
		return result;
	}

	@Override
	public List<Map<String, Object>> getTradLogsOfUser(String userUuid) throws Exception {
	  List<MongoUiTrdLogDTO> tradeLogList = this.getTradeLogs(userUuid);
		List<Map<String,Object>> tradeLogs = new ArrayList<Map<String,Object>>();
		if(tradeLogList==null||tradeLogList.size()==0){
			throw new UserInfoException("404", "交易记录为空");
		}
		Map<Long,Map<String,Object>> bakMap = new HashMap<Long,Map<String,Object>>();
		Map<String,Map<String,Object>> tradLogsMap = new HashMap<String,Map<String,Object>>();
		Map<String,Map<String,Object>> tradLogsMap2 = new HashMap<String,Map<String,Object>>();
		// 获取最新一天的单个基金的信息
		for (MongoUiTrdLogDTO mongoUiTrdLogDTO : tradeLogList) {
			Map<String, Object> map = new HashMap<String, Object>();
			try {
				Long prodId = mongoUiTrdLogDTO.getUserProdId();
				if (mongoUiTrdLogDTO.getFundCode() == null) {
					continue;
				}
				String fundCode = mongoUiTrdLogDTO.getFundCode();
				long dateLong = mongoUiTrdLogDTO.getLastModifiedDate();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date(dateLong);
				String dateTime = simpleDateFormat.format(date);
				map.put("date", dateTime);
				dateLong = dateLong / 1000;
				map.put("dateLong", dateLong);
				String key = prodId + "-" + fundCode;
				if (tradLogsMap.containsKey(key)) {
					if (tradLogsMap.get(key) != null) {
						Map<String, Object> map2 = tradLogsMap.get(key);
						if (map2.get("dateLong") != null) {
							long dateLongold = (long) map2.get("dateLong");
							if (dateLong < dateLongold) {
								continue;
							}
						}
					}
				}
				map.put("operations", TrdOrderOpTypeEnum.getComment(mongoUiTrdLogDTO.getOperations()));
				if (mongoUiTrdLogDTO.getOperations() == 1) {
					map.put("operationsStatus", 1);
				} else if (mongoUiTrdLogDTO.getOperations() == 2) {
					map.put("operationsStatus", 2);
				} else if (mongoUiTrdLogDTO.getOperations() == 3
						|| mongoUiTrdLogDTO.getOperations() == 4) {
					map.put("operationsStatus", 3);
				} else {
					map.put("operationsStatus", 4);
				}
				map.put("tradeStatus", mongoUiTrdLogDTO.getTradeStatus());
				map.put("prodId", prodId);
				if (prodId != null && prodId != 0) {
					ProductsDTO products = this.findByProdId(prodId + "");
					logger.info("理财产品findByProdId查询end");
					if (products == null) {
						map.put("prodName", "");
					} else {
						map.put("prodName", products.getProdName());
					}
				} else {
					map.put("prodName", "");
				}
				if (mongoUiTrdLogDTO.getAmount() != null) {
					map.put("amount", mongoUiTrdLogDTO.getAmount());
				} else if (mongoUiTrdLogDTO.getTradeTargetSum() != null
						&& mongoUiTrdLogDTO
								.getTradeStatus() == TrdOrderStatusEnum.PAYWAITCONFIRM
										.getStatus()) {
					map.put("amount", TradeUtil.getBigDecimalNumWithDiv100(
							mongoUiTrdLogDTO.getTradeTargetSum()));
				} else if (mongoUiTrdLogDTO.getTradeConfirmShare() != null
						&& mongoUiTrdLogDTO
								.getTradeStatus() == TrdOrderStatusEnum.SELLWAITCONFIRM
										.getStatus()) {
					map.put("amount", TradeUtil.getBigDecimalNumWithDiv100(
							mongoUiTrdLogDTO.getTradeTargetShare()));
				} else if (mongoUiTrdLogDTO.getTradeConfirmShare() != null) {
					map.put("amount", new BigDecimal(
							mongoUiTrdLogDTO.getTradeConfirmShare()));
				} else if (mongoUiTrdLogDTO.getTradeConfirmSum() != null) {
					map.put("amount", new BigDecimal(
							mongoUiTrdLogDTO.getTradeConfirmSum()));
				} else {
					logger.error(
							"there is no amount information for mondUiTrdLogDTO with userId:"
									+ userUuid + " userProdId:"
									+ mongoUiTrdLogDTO.getUserProdId());
				}
				tradLogsMap.put(key, map);
			} catch (Exception ex) {
				logger.error(ex.getMessage());
				ex.printStackTrace();
				continue;
			}
			// tradeLogs.add(map);
		}
		
		if (tradLogsMap != null && tradLogsMap.size() > 0) {
			for (String key : tradLogsMap.keySet()) {
				String[] params = key.split("-");
				String prodId = params[0];
				// String fundCode = params[1];
				Map<String, Object> bakMap2 = tradLogsMap.get(key);
				if (!tradLogsMap2.containsKey(prodId)) {
					tradLogsMap2.put(prodId, bakMap2);
				} else {
					Map<String, Object> trad = tradLogsMap2.get(prodId);
					if (trad.get("amount") != null) {
						BigDecimal amountTotal = new BigDecimal(trad.get("amount") + "");
						if (bakMap2.get("amount") != null) {
							amountTotal = amountTotal.add(new BigDecimal(bakMap2.get("amount") + ""));
						}
						trad.put("amount", amountTotal);
					}

					if (trad.get("tradeStatus") != null) {
						Integer operationsStatusOld = Integer.parseInt(trad.get("tradeStatus") + "");
						Integer operationsStatusNew = Integer.parseInt(bakMap2.get("tradeStatus") + "");
						if (operationsStatusOld != null) {
							if (operationsStatusOld == TrdOrderStatusEnum.FAILED.getStatus()
									|| operationsStatusNew == TrdOrderStatusEnum.FAILED.getStatus()) {
								trad.put("tradeStatusComment", TrdOrderStatusEnum.FAILED.getComment());
							} else {
								if (bakMap2.get("tradeStatus") != null) {
									if (operationsStatusNew == TrdOrderStatusEnum.PARTIALCONFIRMED.getStatus()) {
										continue;
									} else {
										if (operationsStatusOld == TrdOrderStatusEnum.CONFIRMED.getStatus()
												&& operationsStatusNew == TrdOrderStatusEnum.CONFIRMED.getStatus()) {
											trad.put("tradeStatusComment", TrdOrderStatusEnum.CONFIRMED.getComment());
										} else if (operationsStatusNew != TrdOrderStatusEnum.CONFIRMED.getStatus()
												&& operationsStatusNew != TrdOrderStatusEnum.FAILED.getStatus()
												&& operationsStatusNew != TrdOrderStatusEnum.CANCEL.getStatus()) {
											trad.put("tradeStatus",TrdOrderStatusEnum.PARTIALCONFIRMED.getStatus());
											trad.put("tradeStatusComment", TrdOrderStatusEnum.PARTIALCONFIRMED.getComment());
										} else {
											trad.put("tradeStatus", TrdOrderStatusEnum.FAILED.getStatus());
											trad.put("tradeStatusComment", TrdOrderStatusEnum.FAILED.getComment());
										}
									}
								} else {
									continue;
								}
							}
						}
					}
				}
			}
			if (tradLogsMap2 != null && tradLogsMap2.size() > 0) {
				for (String key2 : tradLogsMap2.keySet()) {
					Map<String, Object> mapThree = tradLogsMap2.get(key2);
					tradeLogs.add(mapThree);
				}
			}
		}
		Collections.sort(tradeLogs, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Long map1value = (Long) o1.get("dateLong");
				Long map2value = (Long) o2.get("dateLong");
				return map2value.compareTo(map1value);
			}
		});
		
		return tradeLogs;
	}
}
