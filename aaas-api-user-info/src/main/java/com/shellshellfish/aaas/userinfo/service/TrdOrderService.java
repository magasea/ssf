package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderInfo;

import java.util.concurrent.ExecutionException;

/**
 * @Author pierre
 * 17-12-28
 */
public interface TrdOrderService {

	TrdOrderInfo   getOrderInfo(String OrderId) throws ExecutionException, InterruptedException;
}
