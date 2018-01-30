package com.shellshellfish.aaas.finance.trade.pay.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.finance.trade.pay.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface FundTradeApiService {

    OpenAccountResult openAccount(String userUuid, String name, String phone, String identityNo, String bankNo, String bankId) throws Exception;

    BuyFundResult buyFund(String userUuid, String tradeAcco, BigDecimal applySum, String outsideOrderNo, String fundCode) throws Exception;

    SellFundResult sellFund(String userUuid, BigDecimal sellNum, String outsideOrderNo, String tradeAcco,
        String fundCode) throws Exception;

    FundConvertResult fundConvert(String userUuid, BigDecimal applyShare, String outsideOrderNo,
        String tradeAcco,String fundCode, String targetFundCode ) throws Exception;

    CancelTradeResult cancelTrade(String userUuid, String applySerial) throws Exception;

    ApplyResult getApplyResultByApplySerial(String userUuid, String applySerial) throws JsonProcessingException;

    ApplyResult getApplyResultByOutsideOrderNo(String userUuid, String outsideOrderNo) throws JsonProcessingException;

    String getAllApplyList(String userUuid) throws JsonProcessingException;

    String getExamContent() throws JsonProcessingException;

    String commitRisk(String userUuid) throws JsonProcessingException;

    String commitRisk(String userUuid, int riskLevel) throws JsonProcessingException;

    String commitFakeAnswer(String userUuid) throws JsonProcessingException;

    String getUserRiskList(String userUuid) throws JsonProcessingException;

    String getFundInfo(String fundCode) throws Exception;

    List<String> getAllFundsInfo() throws Exception;

    String getTradeRate(String fundCode, String businFlag) throws JsonProcessingException;

    List<TradeRateResult> getTradeRateAsList(String fundCode, String businFlag) throws JsonProcessingException;

    List<TradeLimitResult> getTradeLimits(String fundCode, String businFlag) throws Exception;

    BigDecimal getDiscount(String fundCode, String businFlag) throws Exception;

    BigDecimal getRate(String fundCode, String businFlag) throws Exception;

    BigDecimal calcPoundageByTotalAmount(BigDecimal totalAmount, BigDecimal rate, BigDecimal discount);

    BigDecimal calcPoundage(BigDecimal amount, BigDecimal rate, BigDecimal discount);

    BigDecimal calcDiscountPoundage(BigDecimal amount, BigDecimal rate, BigDecimal discount);

    List<UserBank> getUserBank(String fundCode) throws Exception;

    List<UserBank> getUserBank(String userId,  String fundCode) throws Exception;

    void writeAllTradeRateToMongoDb() throws Exception;

    void writeFundToMongoDb(String json);

    void writeAllFundsToMongoDb(List<String> funds);


    public List<ConfirmResult> getConfirmResults(String openId, String outSideOrderNo) throws
        Exception;

    //String getAllConfirmList(String userUuid) throws JsonProcessingException;


}
