package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.MathUtil;
import com.shellshellfish.aaas.datacollect.DailyFunds;
import com.shellshellfish.aaas.datacollect.DailyFundsCollection;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery.Builder;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc.DataCollectionServiceBlockingStub;
import com.shellshellfish.aaas.finance.trade.order.model.DistributionResult;
import com.shellshellfish.aaas.finance.trade.order.model.FundAmount;
import com.shellshellfish.aaas.finance.trade.order.model.TradeLimitResult;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdCalcService;
import com.shellshellfish.aaas.grpc.common.UserProdDetail;
import com.shellshellfish.aaas.userinfo.grpc.GetUserProdDetailQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc.UserInfoServiceBlockingStub;
import io.grpc.ManagedChannel;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shellshellfish.aaas.finance.trade.order.util.BusinFlag.BUY_FUND;
import static com.shellshellfish.aaas.finance.trade.order.util.BusinFlag.SELL_FUND;

@Service
public class FinanceProdCalcServiceImpl implements FinanceProdCalcService {

    private static final Logger logger = LoggerFactory.getLogger(FinanceProdCalcServiceImpl.class);
    @Autowired
    ManagedChannel managedDccChannel;
    @Autowired
    ManagedChannel managedUIChannel;
    @Autowired
    private FundInfoZhongZhengApiService fundInfoService;

    DataCollectionServiceBlockingStub dataCollectionServiceBlockingStub;

    UserInfoServiceBlockingStub userInfoServiceBlockingStub;

    @PostConstruct
    void init(){
        dataCollectionServiceBlockingStub = DataCollectionServiceGrpc.newBlockingStub(managedDccChannel);
        userInfoServiceBlockingStub=UserInfoServiceGrpc.newBlockingStub(managedUIChannel);
    }
    @Override
    public BigDecimal getMinBuyAmount(List<ProductMakeUpInfo> productMakeUpInfoList)
        throws Exception {
        List<BigDecimal> minAmountList = new ArrayList<>();
        for (ProductMakeUpInfo info : productMakeUpInfoList) {
            List<TradeLimitResult> results = fundInfoService
                .getTradeLimits(info.getFundCode(), BUY_FUND.getCode());
            if (results != null && results.size() > 0) {
                TradeLimitResult tradeLimitResult = results.get(0);
                Double minValue = Double.parseDouble(tradeLimitResult.getMinValue());
                if (info.getFundShare() != 0) {
                    minAmountList
                        .add(BigDecimal.valueOf(minValue / (info.getFundShare() / 10000d)));
                }
            }
        }
        logger.info("{}", minAmountList);
        if (minAmountList == null || minAmountList.size() == 0) {
            return new BigDecimal(0);
        } else {
            return Collections.max(minAmountList);
        }
    }

    @Override
    public BigDecimal getMaxBuyAmount(List<ProductMakeUpInfo> productMakeUpInfoList)
        throws Exception {
        List<BigDecimal> maxAmountList = new ArrayList<>();
        for (ProductMakeUpInfo info : productMakeUpInfoList) {
            List<TradeLimitResult> results = fundInfoService
                .getTradeLimits(info.getFundCode(), BUY_FUND.getCode());
            if (results != null && results.size() > 0) {
                TradeLimitResult tradeLimitResult = results.get(0);
                Double maxValue = Double.parseDouble(tradeLimitResult.getMaxValue());
                if (info.getFundShare() != 0) {
                    maxAmountList
                        .add(BigDecimal.valueOf(maxValue / (info.getFundShare() / 10000d)));
                }
            }
        }
        logger.info("{}", maxAmountList);
        if (maxAmountList == null || maxAmountList.size() == 0) {
            return new BigDecimal(0);
        } else {
            return Collections.min(maxAmountList);
        }
    }

    @Override
    public DistributionResult getPoundageOfBuyFund(BigDecimal grossAmount,
        List<ProductMakeUpInfo> productMakeUpInfoList) throws Exception {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        BigDecimal totalPoundage = BigDecimal.ZERO;
        BigDecimal totalDiscountSaving = BigDecimal.ZERO;
        BigDecimal oldBuyRate = BigDecimal.ZERO;
        BigDecimal discountBuyRate = BigDecimal.ZERO;
        List<FundAmount> fundAmountList = new ArrayList<>();
        Map<Object, Object> buyRateMap = new HashMap<>();
        Collections.sort(productMakeUpInfoList, new Comparator<ProductMakeUpInfo>() {
            public int compare(ProductMakeUpInfo o1, ProductMakeUpInfo o2) {
                if (o1.getFundCode() == o2.getFundCode())
                    return 0;
                return o1.getFundCode().compareToIgnoreCase(o2.getFundCode()) < 0 ? -1 : 1;
            }
        });
        BigDecimal remainAmount = grossAmount;
        for (int idx = 0; idx < productMakeUpInfoList.size(); idx++) {
            ProductMakeUpInfo info = productMakeUpInfoList.get(idx);
            BigDecimal amount = MathUtil.round(grossAmount.multiply(BigDecimal.valueOf(info
                .getFundShare()).divide(BigDecimal.valueOf(10000d))), 2, true);
            if (idx == productMakeUpInfoList.size() - 1) {
                logger.info("now adjust last one amount:{} remainAmount:{}", amount, remainAmount);
                amount = remainAmount;
            } else {
                remainAmount = remainAmount.subtract(amount);
            }
            BigDecimal rate = fundInfoService
                .getRateOfBuyFund(amount, info.getFundCode(), BUY_FUND.getCode());
            BigDecimal discount = fundInfoService
                .getDiscount(info.getFundCode(), BUY_FUND.getCode());
            BigDecimal poundage = fundInfoService.calcPoundageByGrossAmount(amount, rate, discount);
            BigDecimal discountSaving = fundInfoService.calcDiscountSaving(amount, rate, discount);

            totalPoundage = totalPoundage.add(poundage);
            totalDiscountSaving = totalDiscountSaving.add(discountSaving);
            BigDecimal fundShare = BigDecimal.valueOf(info.getFundShare())
                .divide(BigDecimal.valueOf(10000));
            oldBuyRate = oldBuyRate.add(rate.multiply(fundShare));
            discountBuyRate = discountBuyRate.add(rate.multiply(discount).multiply(fundShare));
            FundAmount fundAmount = new FundAmount(info.getFundCode(), info.getFundName(), amount,
                df.format(fundShare.multiply(new BigDecimal("100"))) + "%");
            fundAmountList.add(fundAmount);
        }


        String oldBuyRateStr = df.format(oldBuyRate.multiply(new BigDecimal(100))) + "%";
        String discountBuyRateStr = df.format(discountBuyRate.multiply(new BigDecimal(100))) + "%";
        buyRateMap.put("oldBuyRate", oldBuyRateStr);
        buyRateMap.put("discountBuyRate", discountBuyRateStr);
        return new DistributionResult(totalPoundage, totalDiscountSaving, fundAmountList,
            buyRateMap);
    }

    @Override
    public DistributionResult getPoundageOfSellFund(BigDecimal netAmount,
        List<ProductMakeUpInfo> productMakeUpInfoList,BigDecimal persent,String prodId) throws Exception {
        BigDecimal totalPoundage = BigDecimal.ZERO;
        BigDecimal totalSellAmount = BigDecimal.ZERO;
        BigDecimal totalDiscountSaving = BigDecimal.ZERO;
        List<FundAmount> fundAmountList = new ArrayList<>();
        List<FundAmount> fundAmountResultList = new ArrayList<>();
        List<String> fundCodeList=new ArrayList<>();
        for (ProductMakeUpInfo info : productMakeUpInfoList) {
            String fundCode = info.getFundCode();
            fundCodeList.add(fundCode);
            BigDecimal amount = netAmount.multiply(
                BigDecimal.valueOf(info.getFundShare()).divide(BigDecimal.valueOf(10000d)));
            BigDecimal rate = fundInfoService
                .getRateOfSellFund(amount,info.getFundCode(), SELL_FUND.getCode());
            BigDecimal discount = fundInfoService
                .getDiscount(info.getFundCode(), SELL_FUND.getCode());
            BigDecimal poundage = fundInfoService.calcPoundageWithDiscount(amount, rate, discount);
            BigDecimal discountSaving = fundInfoService
                .calcPoundageWithDiscount(amount, rate, discount);

            totalPoundage = totalPoundage.add(poundage);
            totalDiscountSaving = totalDiscountSaving.add(discountSaving);

            FundAmount fundAmount = new FundAmount(info.getFundCode(), info.getFundName(),
                amount.subtract(poundage),null);
            fundAmountList.add(fundAmount);
        }
        HashMap<Object, Object> fundInfoMap = getNavadjByFundCodeAndDate(fundCodeList);
        //获取基金当前持有份额
        HashMap<Object, Object> userProdDetailMap = getFundConShare(prodId);
        //存入当前持仓金额及预期赎回金额
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
        for(FundAmount fundAmount:fundAmountList){
            if(fundInfoMap.get(fundAmount.getFundCode())!=null){
                HashMap<Object, Object> fundMap=(HashMap<Object, Object>)fundInfoMap.get(fundAmount.getFundCode());
                BigDecimal navadj = new BigDecimal(String.valueOf(fundMap.get("navadj")));
                if(userProdDetailMap.get(fundAmount.getFundCode())!=null){
                    HashMap<Object, Object>  tempUserProdDetailMap =(HashMap<Object, Object> ) userProdDetailMap.get(fundAmount.getFundCode());
                    if(fundAmount.getFundCode().equals(tempUserProdDetailMap.get("fundcode"))){
                        BigDecimal conPostAmount = navadj.multiply(
                            new BigDecimal((Integer) tempUserProdDetailMap.get("fundQuantity"))
                                .divide(new BigDecimal(100d))).setScale(2,BigDecimal.ROUND_HALF_UP);
                        if(conPostAmount.compareTo(new BigDecimal("0"))<=0){
                            logger.error("赎回时计算当前持仓金额: fundcode:"+fundAmount.getFundCode()+"获取当前持仓份额小于等于0");
                        }else{
                            fundAmount.setConPosAmount(conPostAmount.toString());
                            BigDecimal exceptPostAmount = conPostAmount.multiply(persent.divide(new BigDecimal(100))).setScale(2,BigDecimal.ROUND_HALF_UP);
                            if("0".equals(persent.toString())){
                                fundAmount.setExpectSellAmount("--");
                            }else {
                                fundAmount.setExpectSellAmount(exceptPostAmount.toString());
                                totalSellAmount=totalSellAmount.add(exceptPostAmount);
                            }
                            fundAmountResultList.add(fundAmount);
                        }
                    }
                }
            }
        }
        return new DistributionResult(totalSellAmount.toString(),totalPoundage, totalDiscountSaving, fundAmountResultList);
    }

    private  HashMap<Object, Object> getFundConShare(String prodId) {
        GetUserProdDetailQuery.Builder builder = GetUserProdDetailQuery.newBuilder();
        builder.setUserProdId(Long.parseLong(prodId));
        List<UserProdDetail> userProdDetailList = userInfoServiceBlockingStub
            .getUserProdDetail(builder.build()).getUserProdDetailList();
        HashMap<Object, Object> userProdDetailMap = new HashMap<>();
        for(UserProdDetail userProdDetail:userProdDetailList){
            HashMap<Object, Object> tempUserProdDetailMap = new HashMap<>();
            tempUserProdDetailMap.put("fundcode",userProdDetail.getFundCode());
            tempUserProdDetailMap.put("fundQuantity",userProdDetail.getFundQuantity());
            userProdDetailMap.put(userProdDetail.getFundCode(),tempUserProdDetailMap);
        }
        return userProdDetailMap;
    }

    @Override
    public Boolean getMaxMinResult(List<ProductMakeUpInfo> productMakeUpInfoList,
        BigDecimal totalAmount)
        throws Exception {
        BigDecimal min = this.getMinBuyAmount(productMakeUpInfoList);
        if (!min.equals(new BigDecimal(0)) && totalAmount.compareTo(min) == -1) {
            logger.error("购买金额小于起购金额！");
            throw new Exception("购买起购金为"+min.setScale(0,BigDecimal.ROUND_UP)+"元");
            //return false;
        }
        BigDecimal max = this.getMaxBuyAmount(productMakeUpInfoList);
        if (!max.equals(new BigDecimal(0)) && totalAmount.compareTo(max) == 1) {
            logger.error("购买金额大于最大金额！");
            throw new Exception("购买最大金额"+max.setScale(0,BigDecimal.ROUND_UP)+"元");
            //return false;
        }
        return true;
    }
    @Override
    public HashMap<Object, Object>  getNavadjByFundCodeAndDate(List<String> fundCodes) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        LocalDate endDate = InstantDateUtil.format(date);
        LocalDate startDate = endDate.plusDays(-7);
        Builder builder = DailyFundsQuery.newBuilder();
        builder.setNavLatestDateStart(String.valueOf(startDate));
        builder.setNavLatestDateEnd(String.valueOf(endDate));
        builder.addAllCodes(fundCodes);
        List<DailyFunds> dailyFundsList = dataCollectionServiceBlockingStub
            .getFundDataOfDay(builder.build()).getDailyFundsList();
        HashMap<Object, Object> fundInfoMap = new HashMap<>();
        for(DailyFunds dailyFund:dailyFundsList){
            String code = dailyFund.getCode();
            HashMap<Object, Object> tempFundInfoMap = new HashMap<>();
                                                tempFundInfoMap.put("navadj",dailyFund.getNavadj());
            tempFundInfoMap.put("querydate",dailyFund.getQuerydate());
            if(fundInfoMap.get(code)==null){
                fundInfoMap.put(code,tempFundInfoMap);
            }else {
                HashMap<Object, Object>  fundmap=(HashMap<Object, Object>) fundInfoMap.get(code);
                //每次都拿最新一天的值
                if((long)fundmap.get("querydate")<dailyFund.getQuerydate()){
                    fundInfoMap.put(code,tempFundInfoMap);
                }
            }
        }
        return  fundInfoMap;
    }
}
