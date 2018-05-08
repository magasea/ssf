package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.finance.trade.order.model.ZZBankInfo;
import com.shellshellfish.aaas.finance.trade.order.service.ZZApiService;
import com.shellshellfish.aaas.tools.zhongzhengapi.BankZhongZhengInfo;
import com.shellshellfish.aaas.tools.zhongzhengapi.EmptyQuery;
import com.shellshellfish.aaas.tools.zhongzhengapi.ZZApiServiceGrpc.ZZApiServiceBlockingStub;
import io.grpc.ManagedChannel;
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
 * Created by developer4 on 2018- 五月 - 07
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
}
