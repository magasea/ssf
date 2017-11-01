package com.shellshellfish.account.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shellshellfish.account.model.Account;

public class AccountServiceImpl implements AccountService {

	@Override
	public List<Account> findAllAccounts(String type, int pageSize, int stPos) {
		// TODO replace with database find_all operation
		
		List<Account> accounts = new ArrayList<Account>();
		for (int x = 0; x < pageSize; x++) {
		    Account account = new Account();
		    account.setName("Test" + (x+1));
		    account.setType(type);
		    account.setId(stPos+x);
		    accounts.add(account);
		}
		return accounts;
	}

	@Override
	public Account findById(long id) {
		// TODO replace with database find operation
		Account account = new Account();
		account.setName("Checking");
		account.setType("Fixed");
		account.setId(id);
		return account;
	}

	@Override
	public long createAccount(Account account) {
		// TODO create in DB and get new id
		return 100;
	}

	@Override
	public Account updateAccount(Account account) {
		// TODO update in DB
		return account;
	}

	@Override
	public Account deleteById(long id) {
		Account account = findById(id);
		// TDO delete from DB
		return account;
	}

	@Override
	public Map<String, Object> operate(Account account, Map<String, Object> request) {
		// TODO
		request.put("result", "processed");
		request.put("account", account);
		return request;
	}
	
	
}
