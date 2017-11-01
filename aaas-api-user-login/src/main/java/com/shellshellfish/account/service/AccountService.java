package com.shellshellfish.account.service;

import java.util.List;
import java.util.Map;

import com.shellshellfish.account.model.Account;

public interface AccountService {
    List<Account> findAllAccounts(String type, int pageSize, int stPos);
    Account findById(long id);
    long createAccount(Account account);
    Account updateAccount(Account account);
    Account deleteById(long id);
	Map<String, Object> operate(Account account, Map<String, Object> request);
	
	
}
