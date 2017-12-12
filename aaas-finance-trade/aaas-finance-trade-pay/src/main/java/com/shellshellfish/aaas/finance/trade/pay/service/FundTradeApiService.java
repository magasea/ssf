package com.shellshellfish.aaas.finance.trade.pay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.finance.trade.pay.model.*;

import java.util.List;

public interface FundTradeApiService {
    OpenAccountResult openAccount(String name, String phone, String identityNo, String bankNo, String bankId) throws Exception;

    BuyFundResult buyFund(String tradeAcco, Double applySum, String outsideOrderNo, String fundCode) throws Exception;

    SellFundResult sellFund(Integer sellNum, String outsideOrderNo, String tradeAcco, String fundCode) throws Exception;

    CancelTradeResult cancelTrade(String applySerial) throws Exception;

    ApplyResult getApplyResultByApplySerial(String applySerial) throws JsonProcessingException;

    ApplyResult getApplyResultByOutsideOrderNo(String outsideOrderNo) throws JsonProcessingException;

    String getAllApplyList() throws JsonProcessingException;

    String getExamContent() throws JsonProcessingException;

    String commitRisk() throws JsonProcessingException;

    String commitFakeAnswer() throws JsonProcessingException;

    String getUserRiskList() throws JsonProcessingException;

    String getFundInfo(String fundCode) throws Exception;

    List<String> getAllFundsInfo() throws Exception;

    String getTradeRate(String fundCode, String buinflag) throws JsonProcessingException;

    List<TradeRateResult> getTradeRateAsList(String fundCode, String businFlag) throws JsonProcessingException;

    List<TradeLimitResult> getTradeLimits(String fundCode, String businflag) throws Exception;

    void writeAllTradeRateToMongoDb() throws Exception;

    void writeFundToMongoDb(String json);

    void writeAllFundsToMongoDb(List<String> funds);
}
