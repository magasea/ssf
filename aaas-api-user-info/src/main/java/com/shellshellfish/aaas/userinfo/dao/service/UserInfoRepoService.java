package com.shellshellfish.aaas.userinfo.dao.service;

import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBriefDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolioDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserProdMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;

public interface UserInfoRepoService {
	UserBaseInfoDTO getUserInfoBase(Long userId);

	UserInfoAssectsBriefDTO getUserInfoAssectsBrief(Long userId);

	List<BankCardDTO> getUserInfoBankCards(Long userId) throws IllegalAccessException, InstantiationException;

	List<UserPortfolioDTO> getUserPortfolios(Long userId) throws IllegalAccessException, InstantiationException;

	BankCardDTO getUserInfoBankCard(String cardNumber);

	BankCardDTO addUserBankcard(UiBankcard uiBankcard) throws Exception;

	List<AssetDailyReptDTO> getAssetDailyRept(Long userId, Long beginDate, Long endDate)
			throws IllegalAccessException, InstantiationException;

	AssetDailyReptDTO addAssetDailyRept(UiAssetDailyRept uiAssetDailyRept);

	List<AssetDailyReptDTO> getAssetDailyReptByUserId(Long userId) throws IllegalAccessException, InstantiationException;

	List<UserPersonMsgDTO> getUiPersonMsg(Long userId) throws IllegalAccessException, InstantiationException;

	List<UserProdMsgDTO> getUiProdMsg(Long prodId) throws IllegalAccessException, InstantiationException;

	Boolean updateUiUserPersonMsg(String msg, Long user_id, Boolean readedStatus);

	List<UserSysMsgDTO> getUiSysMsg() throws IllegalAccessException, InstantiationException;

	Long getUserIdFromUUID(String userUuid) throws Exception;

	UserPersonMsgDTO addUiPersonMsg(UiPersonMsg uiPersonMsg);

	Page<UiTrdLog> findTradeLogDtoByUserId(Pageable pageable, Long userId);

	Iterable<TradeLogDTO> addUiTrdLog(List<UiTrdLog> trdLogs) throws IllegalAccessException, InstantiationException;

	List<UserInfoFriendRuleDTO> getUiFriendRule(Long bankId) throws IllegalAccessException, InstantiationException;

	UiCompanyInfo getCompanyInfo(Long id);

	UiCompanyInfo addCompanyInfo(UiCompanyInfo uiCompanyInfo);

	Boolean deleteBankCard(String userUuid, String cardNumber);

	Boolean saveUser(String userUuid, String cellphone, String isTestFlag);

	UserBaseInfoDTO findByCellphone(String cellphone);

	Boolean updateCellphone(String cellphone, String isTestFlag, String riskLevel);

	public UserSysMsgDTO addUiSysMsg(UiSysMsg uiSysMsg) throws IllegalAccessException,
			InstantiationException;

	List<TradeLogDTO> findTradeLogDtoByUserId(Long userId) throws IllegalAccessException, InstantiationException;

	ProductsDTO findByProdId(String prodId) throws IllegalAccessException, InstantiationException;
	
	List<ProductsDTO> findTradeLogDtoByUserId(String userId) throws IllegalAccessException, InstantiationException;

	String findUserUUIDByUserId(Long userId);
}