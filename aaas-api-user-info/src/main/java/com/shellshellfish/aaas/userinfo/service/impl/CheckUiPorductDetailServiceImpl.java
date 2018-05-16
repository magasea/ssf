package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.CheckUiProductDetailService;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author pierre.chen
 * @Date 18-5-16
 */
@Service
public class CheckUiPorductDetailServiceImpl implements CheckUiProductDetailService {


    @Autowired
    UiProductRepo uiProductRepo;

    @Autowired
    OrderRpcService orderRpcService;

    @Autowired
    UiProductDetailRepo uiProductDetailRepo;


    Logger logger = LoggerFactory.getLogger(CheckUiPorductDetailServiceImpl.class);

    @Override
    public void check() {
        List<Long> userProdIdList = uiProductRepo.getAllId();

        for (Long userProdId : userProdIdList) {
            List<OrderDetail> orderDetailList = orderRpcService.getAllTrdOrderDetail(userProdId);
            Map fundQuantityMap = calcualteFundQuantity(orderDetailList);
            if (CollectionUtils.isEmpty(fundQuantityMap)) {
                continue;
            }
            Map statusMap = getOrderDetailStatus(userProdId);
            updateFundQuantity(fundQuantityMap, statusMap, userProdId);
        }
    }

    private Map getOrderDetailStatus(Long userProdId) {
        List<OrderDetail> orderDetailList = orderRpcService.getLatestOrderDetail(userProdId);
        Map statusMap = new HashMap(8);
        for (OrderDetail orderDetail : orderDetailList) {
            statusMap.put(orderDetail.getFundCode(), orderDetail.getOrderDetailStatus());
        }
        return statusMap;
    }


    /**
     * 计算当前用户所持有的份额
     *
     * @param orderDetailList
     * @return Map  key = fundCode value = fundQuantity
     */
    private Map calcualteFundQuantity(List<OrderDetail> orderDetailList) {
        Long fundQuantity;
        Map<String, Long> fundQuantityMap = new HashMap<>(8);
        for (OrderDetail orderDetail : orderDetailList) {
            fundQuantity = Optional.of(fundQuantityMap).map(m -> m.get(orderDetail.getFundCode())).orElse(0L);
            int status = orderDetail.getOrderDetailStatus();
            if (!TrdOrderStatusEnum.isConfirmed(status)) {
                continue;
            }
            if (TrdOrderOpTypeEnum.BUY.getOperation() == orderDetail.getTradeType()) {
                fundQuantity += orderDetail.getFundNumConfirmed();
            } else if (TrdOrderOpTypeEnum.REDEEM.getOperation() == orderDetail.getTradeType()) {
                fundQuantity -= orderDetail.getFundNumConfirmed();
            }
            fundQuantityMap.put(orderDetail.getFundCode(), fundQuantity);
        }
        return fundQuantityMap;
    }

    /**
     * 更新用户所持有的基金份额
     *
     * @param fundQuantityMap key = ${fundCode}   value = ${fundQuantity}
     * @param userProdId
     * @return 更新的数据条数
     */
    private int updateFundQuantity(Map<String, Long> fundQuantityMap, Map<String, Integer> statusMap, Long userProdId) {
        int total = 0;
        for (String key : fundQuantityMap.keySet()) {
            Long fundQuantity = fundQuantityMap.get(key);
            Integer status = statusMap.get(key);
            int num = uiProductDetailRepo.updateFundQuantity(key, fundQuantity.intValue(), status, userProdId);
            if (num > 0) {
                total += num;
                logger.info("update fundQuantity of ui_product_details  fundCode :{},fundQuantity:{},status:{}," +
                                "userProdId:{}",
                        key, fundQuantity, status, userProdId);
            }
        }
        return total;
    }
}
