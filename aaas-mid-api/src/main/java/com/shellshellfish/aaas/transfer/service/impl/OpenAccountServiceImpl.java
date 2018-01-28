package com.shellshellfish.aaas.transfer.service.impl;

import com.shellshellfish.aaas.transfer.service.OpenAccountService;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author pierre
 * 18-1-20
 */
public class OpenAccountServiceImpl implements OpenAccountService {


	@Value("${shellshellfish.trade-order-url}")
	private String tradePayUrl;


	@Override
	public void openAccount(String userUuid, String name, String phone, String identityNo, String bankNo, String bankId) {

	}
}
