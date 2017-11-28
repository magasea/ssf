package com.shellshellfish.aaas.userinfo.dao.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dto.BankCard;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLog;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRule;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolio;
import com.shellshellfish.aaas.userinfo.model.dto.UserProdMsg;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsg;

public interface UserInfoRepoService {
	UserBaseInfo getUserInfoBase(Long userId);

	UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId);

	List<BankCard> getUserInfoBankCards(Long userId) throws IllegalAccessException, InstantiationException;

	List<UserPortfolio> getUserPortfolios(Long userId) throws IllegalAccessException, InstantiationException;

	BankCard getUserInfoBankCard(String cardNumber);

	BankCard addUserBankcard(UiBankcard uiBankcard);

	List<AssetDailyRept> getAssetDailyRept(Long userId, Long beginDate, Long endDate)
			throws IllegalAccessException, InstantiationException;

	AssetDailyRept addAssetDailyRept(UiAssetDailyRept uiAssetDailyRept);

	List<AssetDailyRept> getAssetDailyReptByUserId(Long userId) throws IllegalAccessException, InstantiationException;

	List<UserPersonMsg> getUiPersonMsg(Long userId) throws IllegalAccessException, InstantiationException;

	List<UserProdMsg> getUiProdMsg(Long prodId) throws IllegalAccessException, InstantiationException;

	Boolean updateUiUserPersonMsg(String msg, Long user_id, Boolean readedStatus);

	List<UserSysMsg> getUiSysMsg() throws IllegalAccessException, InstantiationException;

	Long getUserIdFromUUID(String userUuid) throws Exception;

	UserPersonMsg addUiPersonMsg(UiPersonMsg uiPersonMsg);

	Page<UiTrdLog> findByUserId(Pageable pageable, Long userId);

	Iterable<TradeLog> addUiTrdLog(List<UiTrdLog> trdLogs) throws IllegalAccessException, InstantiationException;

	List<UserInfoFriendRule> getUiFriendRule(Long bankId) throws IllegalAccessException, InstantiationException;

	UiCompanyInfo getCompanyInfo(Long id);

	UiCompanyInfo addCompanyInfo(UiCompanyInfo uiCompanyInfo);
}
