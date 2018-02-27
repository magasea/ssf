package com.shellshellfish.aaas.finance.trade.order.service;


import com.shellshellfish.aaas.userinfo.grpc.SellProducts;
import com.shellshellfish.aaas.userinfo.grpc.SellProductsResult;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import java.util.concurrent.ExecutionException;

public interface UserInfoService {

  UserBankInfo getUserBankInfo(Long userId) throws ExecutionException, InterruptedException ;

  UserInfo getUserInfoByUserId(Long userId) throws ExecutionException, InterruptedException;

  SellProductsResult checkSellProducts(SellProducts sellProducts)
      throws ExecutionException, InterruptedException;

  SellProducts rollbackSellProducts(SellProducts sellProducts)
      throws ExecutionException, InterruptedException;

  public UserBankInfo getUserBankInfo(String userUUID) throws ExecutionException,
      InterruptedException;
}
