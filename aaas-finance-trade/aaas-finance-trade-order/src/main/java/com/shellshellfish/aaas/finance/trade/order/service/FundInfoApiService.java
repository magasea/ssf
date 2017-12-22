package com.shellshellfish.aaas.finance.trade.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.finance.trade.order.model.TradeLimitResult;
import com.shellshellfish.aaas.finance.trade.order.model.TradeRateResult;
import com.shellshellfish.aaas.finance.trade.order.model.UserBank;

import java.math.BigDecimal;
import java.util.List;

public interface FundInfoApiService {

    String getExamContent() throws JsonProcessingException;

    String commitRisk(String userUuid) throws JsonProcessingException;

    String commitFakeAnswer(String userUuid) throws JsonProcessingException;

    String getUserRiskList(String userUuid) throws JsonProcessingException;

    String getFundInfo(String fundCode) throws Exception;

    List<String> getAllFundsInfo() throws Exception;

    String getTradeRate(String fundCode, String businFlag) throws JsonProcessingException;

    List<TradeRateResult> getTradeRateAsList(String fundCode, String businFlag) throws JsonProcessingException;

    List<TradeLimitResult> getTradeLimits(String fundCode, String businFlag) throws Exception;

    BigDecimal getDiscount(String fundCode, String businFlag) throws Exception;

    BigDecimal getRateOfBuyFund(String fundCode, String businFlag) throws Exception;

    BigDecimal getRateOfSellFund(String fundCode, String businFlag) throws Exception;

    BigDecimal calcPoundageByTotalAmount(BigDecimal totalAmount, BigDecimal rate, BigDecimal discount);

    BigDecimal calcPoundageSaving(BigDecimal amount, BigDecimal rate, BigDecimal discount);

    BigDecimal calcDiscountSaving(BigDecimal amount, BigDecimal rate, BigDecimal discount);

    List<UserBank> getUserBank(String fundCode) throws Exception;


}
