package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankcardDetailBodyDTO;

/**
 * @Author pierre 18-1-20
 */
public interface OpenAccountService {

	String openAccount(BankcardDetailBodyDTO bankcardDetailBodyDTO);

	BankCardDTO createBankCard(BankcardDetailBodyDTO bankcardDetailBodyDTO);
}
