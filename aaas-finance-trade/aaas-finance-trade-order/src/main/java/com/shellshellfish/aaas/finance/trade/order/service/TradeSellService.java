package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdDtlSellPageDTO;

/**
 * Created by chenwei on 2017- 十二月 - 28
 */

public interface TradeGrpcService {
  public void sellProd(com.shellshellfish.aaas.finance.trade.grpc.SellProdInfo request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.finance.trade.grpc.SellProdResult>
          responseObserver);

  Boolean sellProduct(ProdDtlSellPageDTO prodDtlSellPageDTO);
}
