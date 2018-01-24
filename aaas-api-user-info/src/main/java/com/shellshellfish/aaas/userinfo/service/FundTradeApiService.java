package com.shellshellfish.aaas.userinfo.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.userinfo.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface FundTradeApiService {

    OpenAccountResult openAccount(String userUuid, String name, String phone, String identityNo, String bankNo, String bankId) throws Exception;

    BuyFundResult buyFund(String userUuid, String tradeAcco, BigDecimal applySum, String outsideOrderNo, String fundCode) throws Exception;

    SellFundResult sellFund(String userUuid, Integer sellNum, String outsideOrderNo, String tradeAcco, String fundCode) throws Exception;

    CancelTradeResult cancelTrade(String userUuid, String applySerial) throws Exception;

    ApplyResult getApplyResultByApplySerial(String userUuid, String applySerial) throws JsonProcessingException;

    ApplyResult getApplyResultByOutsideOrderNo(String userUuid, String outsideOrderNo) throws JsonProcessingException;

    String getAllApplyList(String userUuid) throws JsonProcessingException;

    ConfirmResult getConfirmResultByApplySerial(String userUuid, String applySerial) throws JsonProcessingException;

    ConfirmResult getConfirmResultByOutsideOrderNo(String userUuid, String outsideOrderNo) throws JsonProcessingException;

    List<ConfirmResult> getConfirmResults(JSONObject jsonObject, Integer status);

    List<ConfirmResult> getConfirmResults(String userUuid, String fundCode,String Date) throws JsonProcessingException;

    //String getAllConfirmList(String userUuid) throws JsonProcessingException;

    String getAllConfirmList(String userUuid, String fundCode, String startDate) throws JsonProcessingException;

    FundNotice getLatestFundNotice(String fundCode) throws Exception;

    List<FundNotice> getFundNotices(String fundCode) throws Exception;

    String getExamContent() throws JsonProcessingException;

    String commitRisk(String userUuid) throws JsonProcessingException;

    String commitFakeAnswer(String userUuid) throws JsonProcessingException;

    String getUserRiskList(String userUuid) throws JsonProcessingException;

    FundInfo getFundInfoAsEntity(String fundCode) throws Exception;

    List<String> getAllFundsInfo() throws Exception;

    String getTradeRate(String fundCode, String businFlag) throws JsonProcessingException;

    List<TradeRateResult> getTradeRateAsList(String fundCode, String businFlag) throws JsonProcessingException;

    String getTradeLimitAsRawString(String fundCode, String businFlag) throws JsonProcessingException;

    List<TradeLimitResult> getTradeLimits(String fundCode, String businFlag) throws Exception;

    String getDiscountRawString(String fundCode, String businFlag) throws Exception;

    BigDecimal getDiscount(String fundCode, String businFlag) throws Exception;

    BigDecimal getRate(String fundCode, String businFlag) throws Exception;

    BigDecimal calcPoundageByTotalAmount(BigDecimal totalAmount, BigDecimal rate, BigDecimal discount);

    BigDecimal calcPoundage(BigDecimal amount, BigDecimal rate, BigDecimal discount);

    BigDecimal calcDiscountPoundage(BigDecimal amount, BigDecimal rate, BigDecimal discount);

    List<UserBank> getUserBank(String fundCode) throws Exception;

    List<BonusInfo> getBonusList(String userUuid, String fundCode, String startDate) throws Exception;

    FundShare getFundShare(String userUuid, String fundCode) throws Exception;

    FundIncome getFundIncome(String userUuid, String fundCode) throws Exception;

    Map<String, BankCardLimitation> getBankCardLimitations();

    BankCardLimitation getBankCardLimitation(String bankName);

    void writeAllTradeRateToMongoDb() throws Exception;

    void writeAllTradeDiscountToMongodDb() throws Exception;

    void writeAllTradeLimitToMongoDb() throws Exception;

    void writeFundToMongoDb(String json);

    void writeAllFundsToMongoDb(List<String> funds);
}
