package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.message.order.ProdDtlSellDTO;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.datamanager.FundInfo;
import com.shellshellfish.aaas.datamanager.FundsInfoServiceGrpc;
import com.shellshellfish.aaas.datamanager.FundsInfoServiceGrpc.FundsInfoServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdDtlSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.service.TradeSellService;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc.UserInfoServiceFutureStub;
import io.grpc.ManagedChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */

public class TradeSellServiceImpl implements TradeSellService {

  @Autowired
  ManagedChannel managedDMChannel;

  FundsInfoServiceFutureStub fundsInfoFutureStub;

  @PostConstruct
  void init(){
    fundsInfoFutureStub = FundsInfoServiceGrpc.newFutureStub(managedDMChannel);
  }

  @Override
  public Boolean sellProduct(ProdSellPageDTO prodSellPageDTO)
      throws ExecutionException, InterruptedException {
    //first : get price of funds , this
    com.shellshellfish.aaas.datamanager.FundCodes.Builder requestBuilder = com.shellshellfish
        .aaas.datamanager.FundCodes.newBuilder();
    for(ProdDtlSellPageDTO prodDtlSellPageDTO: prodSellPageDTO.getProdDtlSellPageDTOList()){
      requestBuilder.addFundCode(prodDtlSellPageDTO.getFundCode());
    }

    List<FundInfo> fundInfoList =fundsInfoFutureStub.getFundsPrice
        (requestBuilder.build()).get().getFundInfoList();

    List<ProdDtlSellDTO> prodDtlSellDTOList = new ArrayList<>();
    for(ProdDtlSellPageDTO prodDtlSellDTO: prodSellPageDTO.getProdDtlSellPageDTOList()){
      ProdSellDTO prodSellDTO = new ProdSellDTO();
      BeanUtils.copyProperties(prodDtlSellDTO, prodDtlSellDTO);
//      prodDtlSellDTO
    }
    return null;
  }
}
