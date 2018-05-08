package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.shellshellfish.aaas.tools.zhongzhengapi.BankZhongZhengInfo;
import com.shellshellfish.aaas.tools.zhongzhengapi.BankZhongZhengInfoList;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApiServiceGrpc;
import com.shellshellfish.aaas.zhongzhengapi.service.ZZGrpcService;
import com.shellshellfish.aaas.zhongzhengapi.service.ZhongZhengApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by developer4 on 2018- 五月 - 08
 */
@Service
public class ZZGrpcServiceImpl extends ZZApiServiceGrpc.ZZApiServiceImplBase implements
    ZZGrpcService {
  @Autowired
  ZhongZhengApiService zhongZhengApiService;



  @Override
  public BankZhongZhengInfoList getSupportBankList() {
    BankZhongZhengInfoList.Builder bzzilBuilder = BankZhongZhengInfoList.newBuilder();
    BankZhongZhengInfo.Builder bzziBuilder = BankZhongZhengInfo.newBuilder();

    zhongZhengApiService.getSupportBankList().forEach(
        item ->{
          bzziBuilder.setBankName(item.getBankName());
          bzziBuilder.setBankSerial(item.getBankSerial());
          bzziBuilder.setCapitalModel(item.getCapitalModel());
          bzziBuilder.setMoneyLimitDay(item.getMoneyLimitDay());
          bzziBuilder.setMoneyLimitOne(item.getMoneyLimitOne());
          bzzilBuilder.addBankZhongZhengInfo(bzziBuilder.build());
          bzziBuilder.clear();
        }
    );
    return bzzilBuilder.build();
  }

  /**
   */
  @Override
  public void getSupportBankList(com.shellshellfish.aaas.tools.zhongzhengapi.EmptyQuery request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.tools.zhongzhengapi.BankZhongZhengInfoList> responseObserver) {
    BankZhongZhengInfoList bankZhongZhengInfoList = getSupportBankList();
    responseObserver.onNext(bankZhongZhengInfoList);
    responseObserver.onCompleted();
  }

}
