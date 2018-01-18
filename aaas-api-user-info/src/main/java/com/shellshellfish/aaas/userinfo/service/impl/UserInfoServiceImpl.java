package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.pay.ZhongZhengQueryByOrderDetailId;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.MongoUiTrdLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBriefDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoCompanyInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolioDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import com.shellshellfish.aaas.userinfo.utils.DateUtil;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import io.grpc.ManagedChannel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

	@Autowired
	UserInfoRepoService userInfoRepoService;

	@Autowired
	UserFinanceProdCalcService userFinanceProdCalcService;
	
	@Autowired
    @Qualifier("secondaryMongoTemplate")
    private MongoTemplate mongoTemplate;

	PayRpcServiceFutureStub payRpcServiceFutureStub;

	@Autowired
	ManagedChannel managedPayChannel;

	@PostConstruct
	void init() {
		payRpcServiceFutureStub = PayRpcServiceGrpc.newFutureStub(managedPayChannel);
	}

    @Override
    public UserBaseInfoDTO getUserInfoBase(String userUuid) throws Exception{
    	Long userId = getUserIdFromUUID(userUuid);
        UserBaseInfoDTO userInfoDao = userInfoRepoService.getUserInfoBase(userId);
//        UserBaseInfo userBaseInfo = new UserBaseInfo();
//        if( null != userInfoDao) {
//            BeanUtils.copyProperties(userInfoDao, userBaseInfo);
//        }
        return userInfoDao;
    }

    @Override
    public UserInfoAssectsBriefDTO getUserInfoAssectsBrief(String userUuid) throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        //UserInfoAssectsBrief userInfoAssectsBrief = new UserInfoAssectsBrief();
        UserInfoAssectsBriefDTO userInfoAssect = userInfoRepoService.getUserInfoAssectsBrief(userId);
//        if(null != userInfoAssect){
//            BeanUtils.copyProperties(userInfoAssect, userInfoAssectsBrief);
//        }
        return userInfoAssect;
    }

    @Override
    public List<BankCardDTO> getUserInfoBankCards(String userUuid) throws Exception {
    	Long userId=null;
    	try{
    	 userId = getUserIdFromUUID(userUuid);
    	}catch(Exception e){
    		throw new UserInfoException("404", "该用户不存在");
    	}
    	List<BankCardDTO> bankcards=null;
    	try{
         bankcards =  userInfoRepoService.getUserInfoBankCards(userId);
    	}catch(Exception e){
    		throw new UserInfoException("404", "该用户暂时没有绑定银行卡");
    	}
//        List<BankCard> bankCardsDto = new ArrayList<>();
//        for(UiBankcard uiBankcard: uiBankcards ){
//            BankCard bankCard = new BankCard();
//            BeanUtils.copyProperties(uiBankcard, bankCard);
//            bankCardsDto.add(bankCard);
//        }
        return bankcards;
    }

    @Override
    public List<UserPortfolioDTO> getUserPortfolios(String userUuid) throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        List<UserPortfolioDTO> userPortfolioDaos =  userInfoRepoService.getUserPortfolios(userId);
//        List<UserPortfolio> userPortfolios = new ArrayList<>();
//        for(UiPortfolio userPortfolioDao: userPortfolioDaos){
//            UserPortfolio userPortfolio = new UserPortfolio();
//            BeanUtils.copyProperties(userPortfolioDao, userPortfolio);
//            userPortfolios.add(userPortfolio);
//        }
        return userPortfolioDaos;
    }

    @Override
    public BankCardDTO getUserInfoBankCard(String cardNumber) throws RuntimeException {
    	BankCardDTO bankCard = userInfoRepoService.getUserInfoBankCard(cardNumber);
//        BankCard bankCard = new BankCard();
//        BeanUtils.copyProperties(uiBankcard, bankCard);
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
        if(!StringUtils.isEmpty(params.get("cardNumber"))){
        	String bankName = BankUtil.getNameOfBank(params.get("cardNumber").toString());
        	if(!StringUtils.isEmpty(bankName)){
        		uiBankcard.setBankName(BankUtil.getNameOfBank(params.get("cardNumber").toString()));
        	}
        }
        BankCardDTO bankCard =  userInfoRepoService.addUserBankcard(uiBankcard);
        return bankCard;
    }

    @Override
    public List<AssetDailyReptDTO> getAssetDailyRept(String userUuid, Long beginDate, Long endDate)
        throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        List<AssetDailyReptDTO> uiAssetDailyRepts = userInfoRepoService.getAssetDailyRept(userId,
            beginDate, endDate);
//        List<AssetDailyRept> assetDailyRepts = new ArrayList<>();
//        for(UiAssetDailyRept uiAssetDailyRept: uiAssetDailyRepts){
//            AssetDailyRept assetDailyRept = new AssetDailyRept();
//            BeanUtils.copyProperties(uiAssetDailyRept, assetDailyRept);
//            assetDailyRept.setDate(new Date(uiAssetDailyRept.getDate()));
//            assetDailyRepts.add(assetDailyRept);
//        }
        return uiAssetDailyRepts;
    }

    @Override
    public AssetDailyReptDTO addAssetDailyRept(AssetDailyReptDTO assetDailyRept){
        UiAssetDailyRept uiAssetDailyRept = new UiAssetDailyRept();
        BeanUtils.copyProperties(assetDailyRept, uiAssetDailyRept);
        uiAssetDailyRept.setDate(assetDailyRept.getDate().getTime());
        AssetDailyReptDTO result =  userInfoRepoService.addAssetDailyRept(uiAssetDailyRept);
//        AssetDailyRept assetDailyReptResult = new AssetDailyRept();
//        BeanUtils.copyProperties(result, assetDailyReptResult);
//        Date date = new Date(result.getDate());
//        assetDailyRept.setDate(date);
        return result;
    }

    @Override
    public List<UserSysMsgDTO> getUserSysMsg(String userUuid) throws IllegalAccessException, InstantiationException {
    	List<UserSysMsgDTO> userSysMsgs = userInfoRepoService.getUiSysMsg();
//        List<UserSysMsg> userSysMsgs = new ArrayList<>();
//        for(UiSysMsg uiSysMsg: uiSysMsgs){
//            UserSysMsg userSysMsg = new UserSysMsg();
//            BeanUtils.copyProperties(uiSysMsg, userSysMsg);
//            userSysMsgs.add(userSysMsg);
//        }
        return userSysMsgs;
    }

    @Override
    public List<UserPersonMsgDTO> getUserPersonMsg(String userUuid) throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        List<UserPersonMsgDTO> uiPersonMsgs = userInfoRepoService.getUiPersonMsg(userId);
//        List<UserPersonMsg> userPersonMsgs = new ArrayList<>();
//        for(UiPersonMsg uiPersonMsg: uiPersonMsgs){
//            UserPersonMsg userPersonMsg = new UserPersonMsg();
//            BeanUtils.copyProperties(uiPersonMsg, userPersonMsg);
//            userPersonMsgs.add(userPersonMsg);
//        }
        return uiPersonMsgs;
    }

    @Override
    public Boolean updateUserPersonMsg(String msgId, String userUuid,
        Boolean readedStatus) throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        Boolean result = userInfoRepoService.updateUiUserPersonMsg(msgId, userId,
            readedStatus);

        return result;
    }

    @Override
    public Page<TradeLogDTO> findByUserId(String userUuid, Pageable pageable) throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        Page<UiTrdLog> tradeLogsPage = userInfoRepoService.findTradeLogDtoByUserId(pageable, userId);
        Page<TradeLogDTO> tradeLogResult = MyBeanUtils.convertPageDTO(pageable, tradeLogsPage, TradeLogDTO.class);
        return tradeLogResult;
    }

    @Override
    public List<UserInfoFriendRuleDTO> getUserInfoFriendRules(Long bankId)
        throws InstantiationException, IllegalAccessException,RuntimeException {
    	List<UserInfoFriendRuleDTO> userInfoFriendRules = userInfoRepoService.getUiFriendRule(bankId);
    	return userInfoFriendRules;
    }

    @Override
    public UserInfoCompanyInfoDTO getCompanyInfo(String userUuid, Long bankId){
        Long id = getCompanyId(userUuid, bankId);
        UiCompanyInfo uiCompanyInfo =  userInfoRepoService.getCompanyInfo(id);
        UserInfoCompanyInfoDTO userInfoCompanyInfo = new UserInfoCompanyInfoDTO();
        if(null == uiCompanyInfo){
            return userInfoCompanyInfo;
        }
        BeanUtils.copyProperties(uiCompanyInfo, userInfoCompanyInfo);
        return userInfoCompanyInfo;

    }
    //TODO: this function will be adjusted by business rule
    private Long getCompanyId(String userUuid, Long bankId){
        return 1L;
    }


    private Long getUserIdFromUUID(String userUuid) throws Exception {
        Long userId =  userInfoRepoService.getUserIdFromUUID(userUuid);
        return userId;
    }

	@Override
	public Boolean deleteBankCard(String userUuid, String bankCardNumber) {
		Boolean result = userInfoRepoService.deleteBankCard(userUuid, bankCardNumber);
		return result;
	}

	@Override
	public Boolean addUiUser(String userUuid, String cellphone, String isTestFlag) {
		Boolean result = userInfoRepoService.saveUser(userUuid, cellphone,isTestFlag);
		return result;
	}

	@Override
	public Boolean updateUiUser(String cellphone, String isTestFlag, String riskLevel) {
		Boolean result = userInfoRepoService.updateCellphone(cellphone,isTestFlag,riskLevel);
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
	public ProductsDTO findByProdId(String prodId) throws IllegalAccessException, InstantiationException {
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
      com.shellshellfish.aaas.finance.trade.pay.ApplyResult result = payRpcServiceFutureStub.queryZhongzhengTradeInfoByOrderDetailId
          (requestBuilder.build()).get();
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
	public Map<String, Object> getTrendYield(String userUuid){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String,Object>> trendYieldList = new ArrayList<Map<String,Object>>();
		Map<String,Object> trendYieldMap = new HashMap();
		String day1 = DateUtil.getSystemDatesAgo(-1);
		String day2 = DateUtil.getSystemDatesAgo(-2);
		BigDecimal rate1 = userFinanceProdCalcService.calcYieldRate(userUuid, day2, day1);

		trendYieldMap.put("date",day1);
		trendYieldMap.put("value",rate1);
		trendYieldList.add(trendYieldMap);
		
		String day3 = DateUtil.getSystemDatesAgo(-3);
		BigDecimal rate2 = userFinanceProdCalcService.calcYieldRate(userUuid, day3, day2);
		trendYieldMap = new HashMap<String, Object>();
		trendYieldMap.put("date",day2);
		trendYieldMap.put("value",rate2);
		trendYieldList.add(trendYieldMap);
		
		String day4 = DateUtil.getSystemDatesAgo(-4);
		BigDecimal rate3 = userFinanceProdCalcService.calcYieldRate(userUuid, day4, day3);
		trendYieldMap = new HashMap<String, Object>();
		trendYieldMap.put("date",day3);
		trendYieldMap.put("value",rate3);
		trendYieldList.add(trendYieldMap);
		
		String day5 = DateUtil.getSystemDatesAgo(-5);
		BigDecimal rate4 = userFinanceProdCalcService.calcYieldRate(userUuid, day5, day4);
		trendYieldMap = new HashMap<String, Object>();
		trendYieldMap.put("date",day4);
		trendYieldMap.put("value",rate4);
		trendYieldList.add(trendYieldMap);
		
		String day6 = DateUtil.getSystemDatesAgo(-6);
		BigDecimal rate5 = userFinanceProdCalcService.calcYieldRate(userUuid, day6, day5);
		trendYieldMap = new HashMap<String, Object>();
		trendYieldMap.put("date",day5);
		trendYieldMap.put("value",rate5);
		trendYieldList.add(trendYieldMap);
		
		String day7 = DateUtil.getSystemDatesAgo(-7);
		BigDecimal rate6 = userFinanceProdCalcService.calcYieldRate(userUuid, day7, day6);
		trendYieldMap = new HashMap<String, Object>();
		trendYieldMap.put("date",day6);
		trendYieldMap.put("value",rate6);
		trendYieldList.add(trendYieldMap);
		resultMap.put("trendYield", trendYieldList);
		return resultMap;
	}
	
	@Override
	public Map<String, Object> getTotalAssets(String uuid) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String yesterday = DateUtil.getSystemDatesAgo(-1);
		String beforeYesterday = DateUtil.getSystemDatesAgo(-2);
		Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(uuid))
                .addCriteria(Criteria.where("date").is(yesterday));

        List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
        if(dailyAmountList!=null&&dailyAmountList.size()>0){
        	BigDecimal asserts = new BigDecimal(0);
        	for(int i=0;i<dailyAmountList.size();i++){
        		DailyAmount dailyAmount = dailyAmountList.get(i);
        		if(dailyAmount.getAsset()!=null){
        			asserts =  asserts.add(dailyAmount.getAsset());
        		}
        	}
        	resultMap.put("assert", asserts);
        	
    		Query query2 = new Query();
            query2.addCriteria(Criteria.where("userUuid").is(uuid))
                    .addCriteria(Criteria.where("date").is(beforeYesterday));
            List<DailyAmount> dailyAmountList2 = mongoTemplate.find(query, DailyAmount.class);
            if(dailyAmountList2!=null&&dailyAmountList2.size()>0){
        		BigDecimal asserts2 = new BigDecimal(0);
        		for(int i=0;i<dailyAmountList.size();i++){
        			DailyAmount dailyIncome = dailyAmountList2.get(i);
            		if(dailyIncome.getAsset()!=null){
            			asserts2 =  asserts2.add(dailyIncome.getAsset());
            		}
            	}
        		resultMap.put("dailyIncome", asserts.subtract(asserts2));
            }
        } else {
        	resultMap.put("assert", 0);
        	resultMap.put("dailyIncome", 0);
        }
        //日收益率
        BigDecimal dailyIncomeRate = userFinanceProdCalcService.calcYieldRate(uuid, beforeYesterday+"", yesterday+"");
        resultMap.put("dailyIncomeRate", dailyIncomeRate);
        //累计收益率
        List<ProductsDTO> productsList = this.findProductInfos(uuid);
        if(productsList!=null&&productsList.size()>0){
        	List<Long> dateList = new ArrayList<Long>();
			for(int i=0;i<productsList.size();i++){
				ProductsDTO products = productsList.get(i);
				dateList.add(products.getUpdateDate());
				System.out.println("--"+products.getUpdateDate());
				//System.out.println("--"+DateUtil.getDateStrFromLong(products.getUpdateDate()));
			}
			Long minDate = Collections.min(dateList);
			String startDate = DateUtil.getDateStrFromLong(minDate).replace("-","");
			BigDecimal incomeRate = userFinanceProdCalcService.calcYieldRate(uuid, startDate, yesterday);
			BigDecimal income = userFinanceProdCalcService.calcYieldValue(uuid, startDate, yesterday);
			resultMap.put("totalIncomeRate", incomeRate);
			resultMap.put("totalIncome", income);
		} else {
			resultMap.put("totalIncomeRate", 0);
			resultMap.put("totalIncome", 0);
		}
        
		return resultMap;
	}
	
	@Override
	public Map<String, Object> getChicombinationAssets(String uuid,ProductsDTO products){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String yesterday = DateUtil.getSystemDatesAgo(-1);
		String beforeYesterday = DateUtil.getSystemDatesAgo(-2);
		Query query = new Query();
		query.addCriteria(Criteria.where("userUuid").is(uuid))
		.addCriteria(Criteria.where("prodId").is(products.getProdId()))
		.addCriteria(Criteria.where("date").is(yesterday));
		
		List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
		if(dailyAmountList!=null&&dailyAmountList.size()>0){
			BigDecimal asserts = new BigDecimal(0);
        	for(int i=0;i<dailyAmountList.size();i++){
        		DailyAmount dailyAmount = dailyAmountList.get(i);
        		if(dailyAmount.getAsset()!=null){
        			asserts =  asserts.add(dailyAmount.getAsset());
        		}
        	}
			resultMap.put("assert", asserts);
			
			Query query2 = new Query();
			query2.addCriteria(Criteria.where("userUuid").is(uuid))
			.addCriteria(Criteria.where("prodId").is(products.getProdId()))
			.addCriteria(Criteria.where("date").is(beforeYesterday));
			List<DailyAmount> dailyAmountList2 = mongoTemplate.find(query, DailyAmount.class);
			if(dailyAmountList2!=null&&dailyAmountList2.size()>0){
				BigDecimal asserts2 = new BigDecimal(0);
        		for(int i=0;i<dailyAmountList.size();i++){
        			DailyAmount dailyIncome = dailyAmountList2.get(i);
            		if(dailyIncome.getAsset()!=null){
            			asserts2 =  asserts2.add(dailyIncome.getAsset());
            		}
            	}
        		resultMap.put("dailyIncome", asserts.subtract(asserts2));
			}
		} else {
        	resultMap.put("assert", 0);
        	resultMap.put("dailyIncome", 0);
        }
		
		//累计收益率
		BigDecimal incomeRate = userFinanceProdCalcService.calcYieldRate(uuid, DateUtil.getDateStrFromLong(products.getUpdateDate()).replace("-", ""), yesterday);
		resultMap.put("totalIncomeRate", incomeRate);
		
		//累计收益
		BigDecimal income = userFinanceProdCalcService.calcYieldValue(uuid, DateUtil.getDateStrFromLong(products.getUpdateDate()).replace("-", ""), yesterday);
		resultMap.put("totalIncome", income);
		
		return resultMap;
	}
	
	@Override
	public List<Map<String, Object>> getTradeLogStatus(String uuid,Long userProdId) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> resultBak = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> resultListBak = new ArrayList<Map<String, Object>>();
		Map<String, Map<String, Object>> resultList = new HashMap<String, Map<String, Object>>();
		Long userId = getUserIdFromUUID(uuid);
		List<MongoUiTrdLogDTO> trdLogList = userInfoRepoService.findByUserIdAndProdId(userId,userProdId);
		if(trdLogList!=null&&trdLogList.size()>0){
			for(int i = 0;i < trdLogList.size();i++){
				MongoUiTrdLogDTO trdLog = trdLogList.get(i);
				int status = trdLog.getTradeStatus();
				String lastModifiedDate = "0";
				if(trdLog.getLastModifiedDate()!=0){
					lastModifiedDate = trdLog.getLastModifiedDate() + "";
				}
				if(resultMap.containsValue(status)){
					if(Long.parseLong(resultMap.get("lastModified")+"")<Long.parseLong(lastModifiedDate)){
						resultMap.put("lastModified", lastModifiedDate);
						resultMap.put("time", DateUtil.getDateType(trdLog.getLastModifiedDate()));
						resultMap.put("status", status+"");
						resultList.put(status+"", resultMap);
					}
				} else {
					resultMap.put("lastModified", lastModifiedDate);
					resultMap.put("time", DateUtil.getDateType(trdLog.getLastModifiedDate()));
					resultMap.put("status", status+"");
					resultListBak.add(resultMap);
				}
			}
			if(resultList!=null&&resultList.size()>0){
				for(Map map:resultList.values()){
					map.remove("lastModified");
					map.remove("lastModified");
					result.add(map);
				}
			}
		}
		return result;
	}
}
