package com.shellshellfish.aaas.userinfo.dao.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shellshellfish.aaas.userinfo.grpc.SellPersentProducts;
import com.shellshellfish.aaas.userinfo.grpc.SellProducts;
import com.shellshellfish.aaas.userinfo.grpc.SellProductsResult.Builder;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.MongoUiTrdLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserProdMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;

public interface UserInfoRepoService {

  UserBaseInfoDTO getUserInfoBase(Long userId);

//	UserInfoAssectsBriefDTO getUserInfoAssectsBrief(Long userId);

  List<BankCardDTO> getUserInfoBankCards(Long userId)
      throws IllegalAccessException, InstantiationException;

  List<BankCardDTO> getUserInfoBankCards(Long userId, String cardNumber) throws
      IllegalAccessException, InstantiationException;

//	List<UserPortfolioDTO> getUserPortfolios(Long userId) throws IllegalAccessException, InstantiationException;

  BankCardDTO getUserInfoBankCard(String cardNumber);

  BankCardDTO addUserBankcard(UiBankcard uiBankcard) throws Exception;

  List<AssetDailyReptDTO> getAssetDailyRept(Long userId, Long beginDate, Long endDate)
      throws IllegalAccessException, InstantiationException;

  AssetDailyReptDTO addAssetDailyRept(UiAssetDailyRept uiAssetDailyRept);

  List<AssetDailyReptDTO> getAssetDailyReptByUserId(Long userId)
      throws IllegalAccessException, InstantiationException;

  List<UserPersonMsgDTO> getUiPersonMsg(Long userId)
      throws IllegalAccessException, InstantiationException;

  List<UserProdMsgDTO> getUiProdMsg(Long prodId)
      throws IllegalAccessException, InstantiationException;

  Boolean updateUiUserPersonMsg(String msg, Long user_id, Boolean readedStatus);

  List<UserSysMsgDTO> getUiSysMsg() throws IllegalAccessException, InstantiationException;

  Long getUserIdFromUUID(String userUuid) throws Exception;

  UserPersonMsgDTO addUiPersonMsg(UiPersonMsg uiPersonMsg);

//	Page<UiTrdLog> findTradeLogDtoByUserId(Pageable pageable, Long userId);
//
//	Iterable<TradeLogDTO> addUiTrdLog(List<UiTrdLog> trdLogs) throws IllegalAccessException, InstantiationException;

  List<UserInfoFriendRuleDTO> getUiFriendRule(Long bankId)
      throws IllegalAccessException, InstantiationException;

//	UiCompanyInfo getCompanyInfo(Long id);

//	UiCompanyInfo addCompanyInfo(UiCompanyInfo uiCompanyInfo);

  Boolean deleteBankCard(String userUuid, String cardNumber);

  Boolean saveUser(String userUuid, String cellphone, String isTestFlag);

  UserBaseInfoDTO findByCellphone(String cellphone);

  Boolean updateCellphone(String cellphone, String isTestFlag, String riskLevel);

  public UserSysMsgDTO addUiSysMsg(UiSysMsg uiSysMsg) throws IllegalAccessException,
      InstantiationException;

//	List<TradeLogDTO> findTradeLogDtoByUserId(Long userId) throws IllegalAccessException, InstantiationException;

  ProductsDTO findByProdId(String prodId) throws IllegalAccessException, InstantiationException;


  Map<String, String> findAllProducts() throws IllegalAccessException, InstantiationException;


  List<ProductsDTO> findTradeLogDtoByUserId(String userId)
      throws IllegalAccessException, InstantiationException;

  String findUserUUIDByUserId(Long userId);

  List<MongoUiTrdLogDTO> findByUserIdAndProdId(Long userId, Long userProdId)
      throws IllegalAccessException, InstantiationException;

  List<MongoUiTrdLogDTO> findByUserProdIdAndOperType(Long userProdId, Integer operType) throws
      IllegalAccessException, InstantiationException;

  public Builder updateProductQuantity(SellProducts request) throws Exception;

  UiUser getUserInfoByUserId(Long userId);

  UiUser getUserInfoByUserUUID(String userUUID) throws Exception;

  List<MongoUiTrdLogDTO> findByUserId(Long userId)
      throws IllegalAccessException, InstantiationException;

  List<MongoUiTrdLogDTO> findAllByUserIdAndUserProdIdAndOperationsAndTradeStatus(Long userId,
      Long userProdId,
      int operations, int tradeStatus) throws IllegalAccessException, InstantiationException;

  UserBaseInfoDTO getUserInfo(String uuid);

  List<MongoUiTrdLogDTO> findByUserProdIdIn(List dataList);

  List<MongoUiTrdLogDTO> findByOrderIdIn(Set orders);

  public Builder updateProductQuantity(SellPersentProducts request) throws Exception;

  Page<UiUser> secectUsers(Pageable pageable) throws InstantiationException, IllegalAccessException;
}