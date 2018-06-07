package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.common.grpc.trade.order.TrdOrderDetail;

import java.util.List;

/**
 * Created by chenwei on 2018- 二月 - 09
 */
public interface OrderRpcService {
    String getBankCardNumberByUserProdId(Long userProdId);

    /**
     * 获取该笔交易所有的操作记录
     *
     * @param userProdId
     * @return
     */
    List<TrdOrderDetail> getAllTrdOrderDetail(Long userProdId);

    /**
     * 获取该笔交易最近的操作记录
     *
     * @param userProdId
     * @return
     */
    List<TrdOrderDetail> getLatestOrderDetail(Long userProdId);

    /**
     *
     * @param orderId
     * @param fundCode
     * @return
     */
    List<TrdOrderDetail> getOrderDetailByGenOrderIdAndFundCode(String orderId, String fundCode);

    /**
     *
     * @param userProdId
     * @param fundCode
     * @param trdType
     * @return
     */
    List<TrdOrderDetail> getOrderDetailByUserProdIdAndFundCodeAndTrdType(Long userProdId, String
        fundCode, Integer trdType);


}
