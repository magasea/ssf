package com.shellshellfish.aaas.finance.trade.pay.service.impl;


import com.shellshellfish.aaas.finance.trade.pay.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.grpc.CardInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserId;
import com.shellshellfish.aaas.userinfo.grpc.UserIdOrUUIDQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserIdOrUUIDQuery.Builder;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc.UserInfoServiceFutureStub;
import io.grpc.ManagedChannel;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2018- 一月 - 17
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {


  UserInfoServiceFutureStub userInfoServiceFutureStub;

  @Autowired
  ManagedChannel managedUIChannel;

  @PostConstruct
  public void init(){
    userInfoServiceFutureStub = UserInfoServiceGrpc.newFutureStub(managedUIChannel);
  }

  @Override
  public UserBankInfo getUserBankInfo(Long userId) throws ExecutionException, InterruptedException {
    UserIdOrUUIDQuery.Builder builder = UserIdOrUUIDQuery.newBuilder();
    builder.setUserId(userId);

    com.shellshellfish.aaas.userinfo.grpc.UserBankInfo userBankInfo =
        userInfoServiceFutureStub.getUserBankInfo(builder.build()).get();
    return userBankInfo;
  }

  @Override
  public String getUserPidForHisTrade(Long userId, String bankCardNum)
      throws ExecutionException, InterruptedException {
    UserBankInfo userBankInfo = getUserBankInfo(userId);
    for(CardInfo cardInfo:userBankInfo.getCardNumbersList()){
      if(cardInfo.getCardNumbers().equals(bankCardNum)){
        return cardInfo.getUserPid();
      }
    }
    return null;
  }

  @Override
  public UserInfo getUserInfoByUserId(Long userId) throws ExecutionException, InterruptedException {
    UserId.Builder uiBuilder = UserId.newBuilder();
    uiBuilder.setUserId(userId);
    return userInfoServiceFutureStub.getUserInfo(uiBuilder.build()).get();

  }


}
