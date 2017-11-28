package com.shellshellfish.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.shellshellfish.bankaccount.grpc.BankAccount;
import com.shellshellfish.gateway.service.BankAccountService;

@RestController
@RequestMapping("/api")
public class RestApiController {
  //public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	BankAccountService bankAccountService;
	
	// Get
	@RequestMapping(value = "/bankaccounts/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map> getAccount(@PathVariable("id") long id) {
		BankAccount account = bankAccountService.getBankAccount(id);
		Map<String, Object> accMap = new HashMap<String, Object>();
		
		if (account == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		accMap.put("accountNumber", account.getAccountNumber());
		accMap.put("balance", account.getBalance());
		return new ResponseEntity<Map>(accMap, HttpStatus.OK);
	}
}
