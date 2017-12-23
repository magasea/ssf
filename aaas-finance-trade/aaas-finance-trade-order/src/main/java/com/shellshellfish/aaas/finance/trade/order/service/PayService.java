package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import java.util.concurrent.ExecutionException;

/**
 * Created by chenwei on 2017- 十二月 - 22
 */
public interface PayService {

  /**
   * 绑银行卡产生tradAcco
   * @param bindBankCard
   * @return
   */
  String bindCard(BindBankCard bindBankCard) throws ExecutionException, InterruptedException;
}
