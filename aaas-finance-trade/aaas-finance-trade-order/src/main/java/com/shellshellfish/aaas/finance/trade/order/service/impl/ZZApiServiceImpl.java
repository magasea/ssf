package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.grpc.zzapi.ApplyResult;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.finance.trade.order.model.ZZBankInfo;
import com.shellshellfish.aaas.finance.trade.order.service.ZZApiService;
import com.shellshellfish.aaas.tools.zhongzhengapi.AplyRltQuery;
import com.shellshellfish.aaas.tools.zhongzhengapi.BankZhongZhengInfo;
import com.shellshellfish.aaas.tools.zhongzhengapi.EmptyQuery;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApiServiceGrpc.ZZApiServiceBlockingStub;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApplyResult;
import io.grpc.ManagedChannel;
import io.grpc.StatusException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Created by chenwei on 2018- 五月 - 07
 */
@Service
public class ZZApiServiceImpl implements ZZApiService {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  ZZApiServiceBlockingStub zzApiServiceBlockingStub;

  @Override
  public List<ZZBankInfo> getZZSupportedBanks() throws Exception {
    EmptyQuery.Builder eqBuilder = EmptyQuery.newBuilder();
    List<BankZhongZhengInfo> bankZhongZhengInfos =  zzApiServiceBlockingStub.getSupportBankList
        (eqBuilder.build()).getBankZhongZhengInfoList();
    List<ZZBankInfo> result = new ArrayList<>();
    if(CollectionUtils.isEmpty(bankZhongZhengInfos)){
      logger.error("zzApiServiceBlockingStub.getSupportBankList got empty list");
      throw new Exception("zzApiServiceBlockingStub.getSupportBankList got empty list");
    }
    for(BankZhongZhengInfo bankZhongZhengInfo: bankZhongZhengInfos){
      ZZBankInfo zzBankInfo = new ZZBankInfo();
      zzBankInfo.setBankCode(bankZhongZhengInfo.getBankSerial());
      zzBankInfo.setBankName(bankZhongZhengInfo.getBankName());
      result.add(zzBankInfo);
    }
    return result;
  }

  @Override
  public List<ApplyResult> getZZApplyResultByApplySerial(String trdAcco, String pid,
      String applySerial) throws Exception {
    AplyRltQuery.Builder arqBuilder = AplyRltQuery.newBuilder();
    arqBuilder.setApplySerial(applySerial);
    arqBuilder.setPid(pid);
    List<ZZApplyResult> zzApplyResults = null;
    try {
      zzApplyResults = zzApiServiceBlockingStub.getAplyResults(arqBuilder
          .build()).getApplyResultList();
    }catch (Exception ex){

      logger.error("error:", ex);
      throw ex;
    }

    List<ApplyResult> applyResults = MyBeanUtils.convertList(zzApplyResults, ApplyResult.class);

    return applyResults;

  }
}
