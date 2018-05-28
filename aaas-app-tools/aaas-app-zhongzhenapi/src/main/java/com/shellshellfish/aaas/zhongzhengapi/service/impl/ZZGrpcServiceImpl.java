package com.shellshellfish.aaas.zhongzhengapi.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.shellshellfish.aaas.common.grpc.zzapi.ApplyResult;
import com.shellshellfish.aaas.common.grpc.zzapi.WalletApplyResult;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.tools.zhongzhengapi.BankZhongZhengInfo;
import com.shellshellfish.aaas.tools.zhongzhengapi.BankZhongZhengInfoList;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApiServiceGrpc;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApplyResult;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApplyResults;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZFundShareInfo;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZFundShareInfoResult;
import com.shellshellfish.aaas.zhongzhengapi.service.ZhongZhengApiService;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Created by chenwei on 2018- 五月 - 08
 */
@Service
public class ZZGrpcServiceImpl extends ZZApiServiceGrpc.ZZApiServiceImplBase  {
  @Autowired
  ZhongZhengApiService zhongZhengApiService;




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
    try{
      BankZhongZhengInfoList bankZhongZhengInfoList = getSupportBankList();
      responseObserver.onNext(bankZhongZhengInfoList);
      responseObserver.onCompleted();
    }catch (Exception ex){
      responseObserver.onError(ex);
    }


  }

  /**
   */
  @Override
  public void getAplyResults(com.shellshellfish.aaas.tools.zhongzhengapi.AplyRltQuery request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.tools.zhongzhengapi.ZZApplyResults> responseObserver) {
//    asyncUnimplementedUnaryCall(getGetAplyResultsMethodHelper(), responseObserver);\
    List<ApplyResult> results = null;
    try {
      if (!StringUtils.isEmpty(request.getApplySerial())) {
        results = getApplyResultsByApplySerial( request.getPid(), request
            .getApplySerial());
      } else if (!StringUtils.isEmpty(request.getOutsideOrderNo())) {
        results = getApplyResultsByOutsideTrdNo(request.getPid(), request
            .getOutsideOrderNo());
      } else {
        results = getApplyResults(request.getPid());
      }
      ZZApplyResults.Builder zzarsBuilder = ZZApplyResults.newBuilder();
      ZZApplyResult.Builder zzarBuilder = ZZApplyResult.newBuilder();
      for(ApplyResult applyResult: results){
        MyBeanUtils.mapEntityIntoDTO(applyResult,zzarBuilder);
        zzarsBuilder.addApplyResult(zzarBuilder);
        zzarBuilder.clear();
      }
      responseObserver.onNext(zzarsBuilder.build());
      responseObserver.onCompleted();
    }catch (Exception ex){
      responseObserver.onError(Status.INTERNAL
          .withDescription(ex.getMessage())
          .augmentDescription("customException()")
          .withCause(ex) // This can be attached to the Status locally, but NOT transmitted to
          // the client!
          .asRuntimeException());
    }
  }


  /**
   */
  public void getFundShare(com.shellshellfish.aaas.tools.zhongzhengapi.ZZFundShareQuery request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.tools.zhongzhengapi.ZZFundShareInfoResult> responseObserver) {
    try {
      List<com.shellshellfish.aaas.common.grpc.zzapi.ZZFundShareInfo> zzFundShareInfos = zhongZhengApiService.getFundShare(request.getPid(),
          request.getFundCode());
      ZZFundShareInfoResult.Builder zzfsirBuilder = ZZFundShareInfoResult.newBuilder();
      ZZFundShareInfo.Builder zzfsiBuilder = ZZFundShareInfo.newBuilder();
      if(CollectionUtils.isEmpty(zzFundShareInfos)){
        zzFundShareInfos.forEach(
            zzFundShareInfo -> {
              zzfsiBuilder.clear();
              MyBeanUtils.mapEntityIntoDTO(zzFundShareInfo, zzfsiBuilder);
              zzfsirBuilder.addZzFundShareInfo(zzfsiBuilder);
            }
        );
      }
      responseObserver.onNext(zzfsirBuilder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      onError(responseObserver, e);
    }
  }

  private void onError(StreamObserver responseObserver, Exception ex){
    responseObserver.onError(Status.INTERNAL
        .withDescription(ex.getMessage())
        .augmentDescription("customException()")
        .withCause(ex) // This can be attached to the Status locally, but NOT transmitted to
        // the client!
        .asRuntimeException());
  }

  public List<ApplyResult> getApplyResultsByApplySerial( String pid,
      String applySerial) throws Exception {
    List<ApplyResult> applyResults = zhongZhengApiService.getApplyResultByApplySerial(applySerial,
        pid);
    return applyResults;
  }




  public List<ApplyResult> getApplyResultsByOutsideTrdNo( String pid,
      String outsideTrdNo) throws Exception {
    List<ApplyResult> applyResults = zhongZhengApiService.getApplyResultByOutSideOrderNo
        (outsideTrdNo,  pid);
    return applyResults;
  }



  public List<ApplyResult> getApplyResults( String pid) throws Exception {
    List<ApplyResult> applyResults = zhongZhengApiService.getApplyResults("","", pid);
    return applyResults;
  }



  public WalletApplyResult applyWallet(String trdAcco, String pid, String applySum,
      String outsideOrderNo) throws Exception {
    return zhongZhengApiService.applyWallet(trdAcco, pid, applySum, outsideOrderNo);
  }



}
