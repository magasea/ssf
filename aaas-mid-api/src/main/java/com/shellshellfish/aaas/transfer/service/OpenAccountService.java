package com.shellshellfish.aaas.transfer.service;

/**
 * @Author pierre
 * 18-1-20
 */
public interface OpenAccountService {

	void openAccount(String userUuid, String name, String phone, String identityNo, String bankNo, String bankId);
}
