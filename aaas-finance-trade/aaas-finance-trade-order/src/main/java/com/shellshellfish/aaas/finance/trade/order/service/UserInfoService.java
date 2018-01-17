package com.shellshellfish.aaas.finance.trade.order.service;


import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import java.util.concurrent.ExecutionException;

public interface UserInfoService {

  UserBankInfo getUserBankInfo(Long userId) throws ExecutionException, InterruptedException ;
}
