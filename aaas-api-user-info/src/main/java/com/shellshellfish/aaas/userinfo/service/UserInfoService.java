package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
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
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserInfoService {
	UserBaseInfoDTO getUserInfoBase(String userUuid) throws Exception;

    UserInfoAssectsBriefDTO getUserInfoAssectsBrief(String userUuid) throws Exception;

    List<BankCardDTO> getUserInfoBankCards(String userUuid) throws Exception;

    List<UserPortfolioDTO> getUserPortfolios(String userUuid) throws Exception;

    BankCardDTO getUserInfoBankCard(String cardNumber);

    BankCardDTO createBankcard(Map params) throws Exception;

    List<AssetDailyReptDTO> getAssetDailyRept(String userUuid, Long beginDate, Long endDate) throws Exception;

    AssetDailyReptDTO addAssetDailyRept(AssetDailyReptDTO assetDailyRept);

    List<UserSysMsgDTO> getUserSysMsg(String userUuid) throws IllegalAccessException, InstantiationException;

    List<UserPersonMsgDTO> getUserPersonMsg(String userUuid) throws Exception;

    Boolean updateUserPersonMsg(String msgId, String userUuid,
        Boolean readedStatus) throws Exception;

    Page<TradeLogDTO> findByUserId(String userUuid, Pageable pageable) throws Exception;

    List<TradeLogDTO> findByUserId(String uuid) throws Exception;
    
    List<UserInfoFriendRuleDTO> getUserInfoFriendRules(Long bankId) throws InstantiationException, IllegalAccessException;

    UserInfoCompanyInfoDTO getCompanyInfo(String userUuid, Long bankId);

	Boolean deleteBankCard(String userUuid, String bankcardId);

	Boolean addUiUser(String userUuid, String cellphone, String isTestFlag);

	//Boolean updateUiUser(String cellphone, String isTestFlag);

	UserBaseInfoDTO selectUiUser(String cellphone);

	Boolean updateUiUser(String cellphone, String isTestFlag, String riskLevel);

	List<ProductsDTO> findProductInfos(String uuid) throws Exception;

	ProductsDTO findByProdId(String prodId) throws IllegalAccessException, InstantiationException;
	
	ApplyResult queryTrdResultByOrderDetailId(Long userId, Long orderDetailId);
	
	Map<String, Object> getTrendYield(String uuid);

	Map<String, Object> getTotalAssets(String uuid) throws Exception;

	Map<String, Object> getChicombinationAssets(String uuid, ProductsDTO productsDTO);

	List<Map<String, Object>> getTradeLogStatus(String uuid, Long userProdId) throws Exception;

	List<MongoUiTrdLogDTO> getTradeLogs(String uuid) throws Exception;

	List<Map<String, Object>> getMyCombinations(String uuid) throws Exception;

}
