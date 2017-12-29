package com.shellshellfish.datamanager.service.impl;

import com.shellshellfish.aaas.common.utils.MathUtil;
import com.shellshellfish.aaas.datamanager.FundCodes;
import com.shellshellfish.aaas.datamanager.FundInfo;
import com.shellshellfish.aaas.datamanager.FundInfos;
import com.shellshellfish.aaas.datamanager.FundsInfoServiceGrpc;
import com.shellshellfish.datamanager.model.FundYeildRate;
import com.shellshellfish.datamanager.service.FundInfoGrpcService;
import com.shellshellfish.datamanager.service.FundInfoService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */
@Service
public class FundInfoGrpcServiceImpl extends FundsInfoServiceGrpc.FundsInfoServiceImplBase implements FundInfoGrpcService {

  @Autowired
  FundInfoService fundInfoService;

  @Override
  public FundInfos getPriceOfCodes(List<String> codes) {
    List<FundYeildRate> fundYeildRateList =  fundInfoService.getLastFundYeildRates4Test(codes);
    List<FundYeildRate> filteredFunds = filter(fundYeildRateList);
    FundInfos.Builder builder = FundInfos.newBuilder();
    FundInfo.Builder builderFI = FundInfo.newBuilder();
    for(FundYeildRate fundYeildRate: filteredFunds){

      builderFI.setNavunit(
          Math.toIntExact(MathUtil.getLongPriceFromDoubleOrig(fundYeildRate.getNavunit())));
      builderFI.setFundCode(fundYeildRate.getCode());
      builder.addFundInfo(builderFI);
    }
    return builder.build();
  }

  private List<FundYeildRate> filter(List<FundYeildRate> fundYeildRateList) {
    Map<String, FundYeildRate> fundYeildRateHashMap = new HashMap<>();
    for(FundYeildRate fundYeildRate: fundYeildRateList){
      if(!fundYeildRateHashMap.containsKey(fundYeildRate.getCode())){
        if(fundYeildRate.getNavunit() != null && fundYeildRate.getNavunit() != Double.MIN_VALUE){
          fundYeildRateHashMap.put(fundYeildRate.getCode(), fundYeildRate);
        }else{
          if(fundYeildRateHashMap.get(fundYeildRate.getCode()).getQuerydate() < fundYeildRate
              .getQuerydate() && fundYeildRate.getNavunit() != Double.MIN_VALUE ){
            fundYeildRateHashMap.put(fundYeildRate.getCode(), fundYeildRate);
          }
        }
      }
    }
    List<FundYeildRate> fundYeildRates = new ArrayList<>();
    for(Entry<String, FundYeildRate> entry: fundYeildRateHashMap.entrySet()){
      fundYeildRates.add(entry.getValue());
    }
    return fundYeildRates;
  }
}
