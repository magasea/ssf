package com.shellshellfish.aaas.transfer.service.impl;

import com.shellshellfish.aaas.common.enums.QDII;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import com.shellshellfish.aaas.finance.trade.order.OrderDetailResult;
import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc;
import com.shellshellfish.aaas.grpc.common.UserProdId;
import com.shellshellfish.aaas.transfer.service.FundGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author pierre.chen
 * @Date 18-5-10
 */
@Service
public class FundGroupServiceImpl implements FundGroupService {

    @Value("${shellshellfish.userinfo-url}")
    private String userinfoUrl;

    @Value("${shellshellfish.data-manager-url}")
    private String datamanagerUrl;

    @Value("${shellshellfish.trade-order-url}")
    private String tradeOrderUrl;


    @Autowired
    OrderRpcServiceGrpc.OrderRpcServiceBlockingStub orderRpcServiceBlockingStub;

    @Autowired
    RestTemplate restTemplate;

    Logger logger = LoggerFactory.getLogger(FundGroupServiceImpl.class);

    private static final String MESSAGE_FORMAT = "资产含%s确认中%s%s，将于%s确认";
    private static final String EMPTY_STRING = "";
    private static final String UNIT_TYPE_YUAN = "元";
    private static final String UNIT_TYPE_FEN = "份";

    @Override
    public Map getMyProductDetail(String userProdId, String uuid, String groupId, String subGroupId) {
        OrderDetailResult orderDetailResult = getOrderDetails(Integer.valueOf(userProdId));
        List<OrderDetail> orderDetailList = orderDetailResult.getOrderDetailResultList();

        if (CollectionUtils.isEmpty(orderDetailList))
            return null;

        Long createDate = orderDetailList.get(0).getCreateDate();
        String buyDate = InstantDateUtil.format(createDate).toString();
        Map result = postForMyProductDetails(uuid, userProdId);

        result.put("groupId", groupId);
        result.put("subGroupId", subGroupId);
        result.put("buyDate", buyDate);

        getBankInfo(result, userProdId, uuid);

        //确认中的基金你数量
        int count = 0;
        //确认中的所有基金份额数量加总
        Long totalFundNum = 0L;
        Long totalFundSum = 0L;
        //组合中含有QDII
        boolean containQDII = false;
        // 交易类型　购买或者赎回
        String tradeType = EMPTY_STRING;

        String unitType = EMPTY_STRING;


        for (OrderDetail orderDetail : orderDetailResult.getOrderDetailResultList()) {
            if (TrdOrderStatusEnum.isInWaiting(orderDetail.getOrderDetailStatus())) {
                count++;
                if (StringUtils.isEmpty(tradeType)) {
                    int orderType = orderDetail.getTradeType();
                    tradeType = TrdOrderOpTypeEnum.getComment(orderType);
                    if (tradeType == TrdOrderOpTypeEnum.REDEEM.getComment()) {
                        unitType = UNIT_TYPE_FEN;
                    } else {
                        unitType = UNIT_TYPE_YUAN;
                    }
                }

                totalFundNum += orderDetail.getFundNum();
                totalFundSum += orderDetail.getFundSum();
            }

            if (!containQDII && QDII.isQDII(orderDetail.getFundCode()))
                containQDII = true;
        }
        Long totals = tradeType.equalsIgnoreCase(TrdOrderOpTypeEnum.REDEEM.getComment()) ? totalFundNum : totalFundSum;
        if (count <= 0 || totals <= 0) {
            result.put("title", EMPTY_STRING);
        } else {
            //组合中包含QDII 确认日期为T+15 否则为T+2
            String date = InstantDateUtil.getTplusNDayNWeekendOfWork(createDate, containQDII ? 15 : 2);
            result.put("title", String.format(MESSAGE_FORMAT, tradeType, TradeUtil.getBigDecimalNumWithDiv100(totals), unitType,
                    date));
        }

        return result;
    }


    private OrderDetailResult getOrderDetails(Integer userProdId) {
        UserProdId.Builder builder = UserProdId.newBuilder();
        builder.setUserProdId(userProdId);
        return orderRpcServiceBlockingStub.getLatestOrderDetail(builder.build());
    }


    private Map postForMyProductDetails(String uuid, String userProdId) {
        final String getMyProductDetailUrl = "/api/userinfo/getMyProductDetail";

        Map result = null;
        Map<String, String> params = new HashMap(3);
        params.put("uuid", uuid);
        params.put("prodId", userProdId);
        ResponseEntity<Map> entity = restTemplate.postForEntity(
                URLutils.prepareParameters(userinfoUrl + getMyProductDetailUrl, params), HttpEntity.EMPTY, Map.class);
        if (HttpStatus.OK.equals(entity.getStatusCode())) {
            result = entity.getBody();
            //format date and add maxValue and minValue
            List<Map> accumulationIncomesList = (List<Map>) result.get("accumulationIncomes");
            if (!CollectionUtils.isEmpty(accumulationIncomesList)) {
                Double maxValue = -Double.MAX_VALUE;
                Double minValue = Double.MAX_VALUE;
                for (int i = 0; i < accumulationIncomesList.size(); i++) {
                    Map accumulationIncomesMap = accumulationIncomesList.get(i);
                    if (accumulationIncomesMap.get("value") != null) {
                        Double value = Double.valueOf(accumulationIncomesMap.get("value").toString());
                        LocalDate date = InstantDateUtil.format(accumulationIncomesMap.get("date").toString(), "yyyyMMdd");
                        accumulationIncomesMap.put("date", date.toString());
                        minValue = minValue > value ? value.doubleValue() : minValue;
                        maxValue = maxValue < value ? value.doubleValue() : maxValue;
                    }
                }
                result.put("maxValue", maxValue);
                result.put("minValue", minValue);
            }

        }
        return result;
    }


    private void getBankInfo(Map result, String userProdId, String uuid) {
        final String bankNumsUrl = "/api/trade/funds/banknums/{uuid}";
        final String bankCardsUrl = "/api/userinfo/users/{uuid}/bankcards";
        final String defaultOrderType = "1";

        Map banknumsParams = new HashMap(2);
        banknumsParams.put("prodId", userProdId);
        banknumsParams.put("orderType", defaultOrderType);
        Map bankNumResult = restTemplate
                .getForEntity(URLutils.prepareParameters(tradeOrderUrl + bankNumsUrl, banknumsParams), Map.class, uuid).getBody();

        if (bankNumResult.get("bankNum") != null) {
            String bankNum = bankNumResult.get("bankNum") + "";
            String bankName = "";
            String telNum = "";
            List bankList = restTemplate.getForEntity(userinfoUrl + bankCardsUrl, List.class, uuid).getBody();

            if (!CollectionUtils.isEmpty(bankList)) {
                for (int i = 0; i < bankList.size(); i++) {
                    Map bankMap = (Map) bankList.get(i);
                    if (bankNum.equals(bankMap.get("bankcardNum"))) {
                        if (bankMap.get("bankShortName") != null) {
                            bankName = bankMap.get("bankShortName") + "";
                            telNum = bankMap.get("cellphone") + "";
                            break;
                        }
                    }
                }
                result.put("bankNum", bankNum);
                result.put("telNum", telNum);
                result.put("bankName", bankName);
            }
        }
    }

}
