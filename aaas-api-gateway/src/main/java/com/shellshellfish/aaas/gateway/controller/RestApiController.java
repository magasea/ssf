package com.shellshellfish.aaas.gateway.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.google.protobuf.Descriptors.FieldDescriptor;
//import com.shellshellfish.bankaccount.grpc.BankAccount;
import com.shellshellfish.aaas.gateway.service.BankAccountService;

@RestController
@RequestMapping("/api")
public class RestApiController {
  //public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	BankAccountService bankAccountService;
	
	// Get
//	@RequestMapping(value = "/bankaccounts/{id}", method = RequestMethod.GET)
//	public ResponseEntity<Map> getAccount(@PathVariable("id") long id) {
//		BankAccount account = bankAccountService.getBankAccount(id);
//		Map<String, Object> accMap = new HashMap<String, Object>();
//
//		if (account == null) {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//		accMap.put("accountNumber", account.getAccountNumber());
//		accMap.put("balance", account.getBalance());
//		return new ResponseEntity<Map>(accMap, HttpStatus.OK);
//	}
	// Get
//	@RequestMapping(value = "/userinfo/friendrules/{bankid}", method = RequestMethod.GET)
//	public HttpServletResponse getAccount(@PathVariable("bankid") long bankid, HttpServletRequest
//			req, HttpServletResponse res) {
//		System.out.println(req.getHeaderNames());
//		System.out.println("------" + req.getHeader("Test"));
//		if (req.getHeader("Test") != null) {
//			res.addHeader("Test", req.getHeader("Test"));
//		}
//		return res;
//	}
}
