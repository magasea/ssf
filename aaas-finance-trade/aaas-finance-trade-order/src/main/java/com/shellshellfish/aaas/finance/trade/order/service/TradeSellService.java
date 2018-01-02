package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdDtlSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPageDTO;
import java.util.concurrent.ExecutionException;

/**
 * Created by chenwei on 2017- 十二月 - 28
 */

public interface TradeSellService {


  TrdOrder sellProduct(ProdSellPageDTO prodSellPageDTO)
      throws ExecutionException, InterruptedException;
}
