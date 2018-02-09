package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankcardDetailBodyDTO;

/**
 * Created by chenwei on 2018- 二月 - 09
 */
public interface OrderRpcService {
  String getBankCardNumberByUserProdId(Long userProdId);

  String openAccount(BankcardDetailBodyDTO bankcardDetailBodyDTO);

  BankCardDTO createBankCard(BankcardDetailBodyDTO bankcardDetailBodyDTO);

}
