package com.shellshellfish.aaas.finance.trade.pay.service;


import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import java.util.concurrent.ExecutionException;

public interface UserInfoService {

  UserBankInfo getUserBankInfo(Long userId) throws ExecutionException, InterruptedException ;

  String getUserPidForHisTrade(Long userId, String bankCardNum)
      throws ExecutionException, InterruptedException;

  UserInfo getUserInfoByUserId(Long userId) throws ExecutionException, InterruptedException;
}
