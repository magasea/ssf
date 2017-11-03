package com.shellshellfish.account.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.Valid;
//import com.shellshellfish.account.Validation.Max;
import com.shellshellfish.account.model.Error;
import com.shellshellfish.account.model.PageSchema;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
//import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shellshellfish.account.model.Account;
import com.shellshellfish.account.service.AccountService;
import com.shellshellfish.account.service.ResourceManager;
import com.shellshellfish.account.service.SchemaManager;


@RestController
@RequestMapping("/api")
@Validated
public class RestApiController {
  //public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	AccountService accountService;
	
	@Autowired
	ResourceManager resourceManager;
	
	@Autowired
	SchemaManager schemaManager;
	
	@Bean
	public ResourceManager resourceManager() {
		return new ResourceManager();
	}
	
	@Bean
	public SchemaManager schemaManager() {
		return new SchemaManager();
	}
	
	@RequestMapping(value = "/register.json", method = RequestMethod.GET)
	public ResponseEntity<PageSchema> registerschema(
			//@Valid @NotNull(message="not null") @Max(value=20) @Min(value=1) @RequestParam(value = "id") Integer bankid
			){
		   
		   PageSchema pageschema= schemaManager.getSchemafile("register");
		  //System.out.println(pagestr);
		  return new ResponseEntity<PageSchema>(pageschema, HttpStatus.OK);	
    }
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ResponseEntity<Map> registerres(
			//@Valid @NotNull(message="not null") @Max(value=20) @Min(value=1) @RequestParam(value = "id") Integer bankid
			){
		   
		  HashMap<String ,Object> rsmap= resourceManager.describe("register");
		  //System.out.println(pagestr);
		  return new ResponseEntity<Map>(rsmap, HttpStatus.OK);	
    }

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseEntity<Map> loginres(
			//@Valid @NotNull(message="not null") @Max(value=20) @Min(value=1) @RequestParam(value = "id") Integer bankid
			){
		   
		  HashMap<String ,Object> rsmap= resourceManager.describe("login");
		  //System.out.println(pagestr);
		  return new ResponseEntity<Map>(rsmap, HttpStatus.OK);	
    }

	// login schema
	@RequestMapping(value = "/login.json", method = RequestMethod.GET)
	public ResponseEntity<PageSchema> loginschema(
			//@Valid @NotNull(message="not null") @Max(value=20) @Min(value=1) @RequestParam(value = "id") Integer bankid
			){
		   
		  PageSchema ps= schemaManager.getSchemafile("login");
		  //System.out.println(pagestr);
		  return new ResponseEntity<PageSchema>(ps, HttpStatus.OK);	
    }

	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public ResponseEntity<List<Account>> listAllAccounts(
    		@Valid @NotNull(message="not null") @Max(value=10 ,message="max is 10") @RequestParam(value = "type") Integer type,
    		@RequestParam(value = "pageSize", defaultValue = "10") String pageSize,
    		@RequestParam(value = "start", defaultValue = "0") String stPos) {
	    
		//if (Integer.parseInt(type)>=5) {
			
		//	RangeValidationException exp=  new RangeValidationException(0);
		//	exp.setUpperLimit("type",type, "5");
		//	throw exp;
		//}
		
		
		
		List<Account> accounts = accountService.findAllAccounts(String.valueOf(type), Integer.parseInt(pageSize), Integer.parseInt(stPos));
	    if (accounts.isEmpty()) {
	    	return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    }
	    return new ResponseEntity<List<Account>>(accounts, HttpStatus.OK);
    }
	
	// Get
	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.GET)
	public ResponseEntity<Account> getAccount(@PathVariable("id") long id) {
		Account account = accountService.findById(id);
		if (account == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Account>(account, HttpStatus.OK);
	}
	
	// Create
	@RequestMapping(value = "/accounts/", method = RequestMethod.POST)
	public ResponseEntity<?> createAccount(@RequestBody Account account, UriComponentsBuilder ucBuilder) {
		long id = accountService.createAccount(account);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/api/acounts/{id}").buildAndExpand(id).toUri());
		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}
	
	// Update
	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Account> updateAccount(@PathVariable("id") long id, @RequestBody Account account) {
		account.setId(id);
		Account updatedAccount = accountService.updateAccount(account);
		return new ResponseEntity<Account>(updatedAccount,HttpStatus.OK);
	}
	
	// Delete
	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Account> deleteAccount(@PathVariable("id") long id) {
		accountService.deleteById(id);
		return new ResponseEntity<Account>(HttpStatus.NO_CONTENT);
	}
	
	// Operation with POSt
	@RequestMapping(value = "/accounts/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> operateAccount(@PathVariable("id") long id, @RequestBody Map<String, Object> request){
		Account account = accountService.findById(id);
		return new ResponseEntity<Map<String, Object>>(accountService.operate(account, request), HttpStatus.OK);
	}
	
	//@ExceptionHandler(MissingServletRequestParameterException.class)
	//@ExceptionHandler(RangeValidationException.class)
	//@ExceptionHandler(ServletRequestBindingException.class)
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Error> RangeErrorHandler(ConstraintViolationException e){
	    //String userId = e.getUserId();
	    //e.getConstraintViolations().
		Set<ConstraintViolation<?>> cons=e.getConstraintViolations();
		ConstraintViolation<?> con=getconstraint(cons);
		String errmsg =con.getMessage();
		//con.g
		int errval=(Integer)con.getInvalidValue();
		Path pname=con.getPropertyPath();
		Iterator<Node> its=pname.iterator();
		
		String paraname= getParameterName(its);
		Error error = new Error(paraname+":"+errmsg);
	    return new ResponseEntity<Error>(error,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Error> ParamMissingErrorHandler(MissingServletRequestParameterException e){
	    String errmsg=e.getMessage();
	    Error error = new Error(errmsg);
	    String pname=e.getParameterName();
	    String ptype= e.getParameterType();
	    return new ResponseEntity<Error>(error,HttpStatus.BAD_REQUEST);
	}
	
	public ConstraintViolation<?> getconstraint(Set<ConstraintViolation<?>> cons) {
		  
		String msg=null;
		Iterator<ConstraintViolation<?>> it = cons.iterator();
		ConstraintViolation<?> con=null;
		if (it.hasNext()) {  
		   con = it.next();
		  //System.out.println(str);  
		}  	
		return con;
	}
	
	
	public String getParameterName(Iterator<Node> its) {
		
		Node node;
		String name="";
		while (its.hasNext()) {  
			   node = its.next();
			   name=name+node.getName()+".";
			   //System.out.println(str);  
		}
		
		return name.substring(0,name.length()-1);
	}
}
