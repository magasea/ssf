package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.MongoUiTrdLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TrendYield;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBriefDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoCompanyInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolioDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserInfoService {

	UserBaseInfoDTO getUserInfoBase(String userUuid) throws Exception;

	UserInfoAssectsBriefDTO getUserInfoAssectsBrief(String userUuid) throws Exception;

	List<BankCardDTO> getUserInfoBankCards(String userUuid);

	List<UserPortfolioDTO> getUserPortfolios(String userUuid) throws Exception;

	BankCardDTO getUserInfoBankCard(String cardNumber);

	BankCardDTO createBankcard(Map params) throws Exception;

	List<AssetDailyReptDTO> getAssetDailyRept(String userUuid, Long beginDate, Long endDate)
			throws Exception;

	AssetDailyReptDTO addAssetDailyRept(AssetDailyReptDTO assetDailyRept);

	List<UserSysMsgDTO> getUserSysMsg(String userUuid)
			throws IllegalAccessException, InstantiationException;

	List<UserPersonMsgDTO> getUserPersonMsg(String userUuid) throws Exception;

	Boolean updateUserPersonMsg(String msgId, String userUuid,
			Boolean readedStatus) throws Exception;

	Page<TradeLogDTO> findByUserId(String userUuid, Pageable pageable) throws Exception;

	List<TradeLogDTO> findByUserId(String uuid) throws Exception;

	List<UserInfoFriendRuleDTO> getUserInfoFriendRules(Long bankId)
			throws InstantiationException, IllegalAccessException;

	UserInfoCompanyInfoDTO getCompanyInfo(String userUuid, Long bankId);

	Boolean deleteBankCard(String userUuid, String bankcardId);

	Boolean addUiUser(String userUuid, String cellphone, String isTestFlag);

	//Boolean updateUiUser(String cellphone, String isTestFlag);

	UserBaseInfoDTO selectUiUser(String cellphone);

	Boolean updateUiUser(String cellphone, String isTestFlag, String riskLevel);

	List<ProductsDTO> findProductInfos(String uuid) throws Exception;

	ProductsDTO findByProdId(String prodId) throws IllegalAccessException, InstantiationException;

	ApplyResult queryTrdResultByOrderDetailId(Long userId, Long orderDetailId);

	/**
	 * 用户累计收益走势图
	 */
	List<TrendYield> getTrendYield(String uuid) throws Exception;

	Map<String, Object> getTotalAssets(String uuid) throws Exception;

	PortfolioInfo getChicombinationAssets(String uuid, Long userid, ProductsDTO productsDTO)
			throws Exception;

	Map<String, Object> getTradeLogStatus(String uuid, Long userProdId) throws Exception;


	List<MongoUiTrdLogDTO> getTradeLogs(String uuid) throws Exception;

	List<Map<String, Object>> getMyCombinations(String uuid) throws Exception;

	Integer getUserRishLevel(String uuid);

	Map<String, Object> getProducts(Long prodId)
			throws IllegalAccessException, InstantiationException;

	List<Map<String, Object>> getTradLogsOfUser(String userUuid) throws Exception;

	PortfolioInfo getChicombinationAssets(String uuid, Long userId, ProductsDTO products,
			LocalDate endDate, boolean flag);

	Map<String, PortfolioInfo> getCalculateTotalAndRate(String uuid, Long userId,
			ProductsDTO products);

    Map<String, Object> getTradLogsOfUser2(String userUuid, Integer pageSize, Integer pageIndex, Integer type);

}
