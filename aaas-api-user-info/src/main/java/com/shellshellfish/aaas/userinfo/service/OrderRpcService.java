package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.finance.trade.order.OrderDetail;

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
    List<OrderDetail> getAllTrdOrderDetail(Long userProdId);

    /**
     * 获取该笔交易最近的操作记录
     *
     * @param userProdId
     * @return
     */
    List<OrderDetail> getLatestOrderDetail(Long userProdId);


}
