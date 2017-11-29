package com.shellshellfish.datamanager.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shellshellfish.datamanager.model.DailyFunds;
import com.shellshellfish.datamanager.model.FundCodes;
import com.shellshellfish.datamanager.service.DataService;
import com.shellshellfish.datamanager.service.DataServiceImpl;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Configuration
@PropertySource(value = "classpath:application.properties",encoding = "utf-8")
@RestController
@RequestMapping("api/datamanager")
@Validated
@Api("基金数据管理相关restapi")
public class RestApiController {
  //public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	private static final Class<?> UserException = null;

	@Autowired
	DataService dataService;
	
	@Value("${daily.fromdate}")
    String deffromdate;
    
	@Value("${daily.todate}")
    String deftodate;
    
	
		
	
	@Bean
	public DataService DataService() {
		return new DataServiceImpl();
	}
	
	
	@RequestMapping(value = "/saveAllfundCodestoDB", method = RequestMethod.GET)
	public ResponseEntity<HttpStatus> saveAllfundCodestoDB(
			//@Valid @NotNull(message="电话不能为空") @Max(value=20) @Min(value=1) @RequestParam(value = "id") Integer bankid
			){
		   
		  if (!dataService.saveAllfundCodestoDB())
			  return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
		   
		  
		  return new ResponseEntity<HttpStatus>(HttpStatus.OK);	
    }
	
    //保存指定基金列表的每天指标到db（某天）
	@RequestMapping(value = "/saveDailytoDBforday", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> saveDailytoDBforday(
			@RequestParam(value = "codes") String codes,
			@RequestParam(value = "curdate") String curdate
			
			){
		   
		    if (!dataService.saveDailytoDBforday(codes,curdate))
		    	return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
		  
	        return new ResponseEntity<HttpStatus>(HttpStatus.OK);	
    }
	
	//保存指定基金列表的每天指标到db（某段时间）
	@RequestMapping(value = "/saveDailytoDBfordays", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> saveDailytoDBfordays(
			@RequestParam(value = "codes") String codes,
			@RequestParam(value = "fromdate") String fromdate,
			@RequestParam(value = "todate") String todate
			
			){
		   
		    if (codes==null || codes=="") {
		    	System.out.println("fund codes are null, set fund codes to all");
		    	codes="all";
		    }
		    
		    if (fromdate==null || fromdate=="") {
		    	System.out.println("fromdate is null, set fromdate to default value");
		    	fromdate=deffromdate;
		    }
		    
		    if (todate==null || todate=="") {
		    	System.out.println("todate is null, set todate to default value");
		    	todate=deftodate;
		    }
		    
		    if (!dataService.saveDailytoDBfordays(codes,fromdate,todate))
		    	return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
		    
	        return new ResponseEntity<HttpStatus>(HttpStatus.OK);	
    }
	
	
	@RequestMapping(value = "/getAllFundCodes", method = RequestMethod.GET)
	public ResponseEntity<List<FundCodes>> getAllFundCodes(){
		  
		  List<FundCodes> fundslst=dataService.getAllFundCodes();
		  return new ResponseEntity<List<FundCodes>>(fundslst,HttpStatus.OK);
    }
	
	@RequestMapping(value = "/getDailyFunds", method = RequestMethod.GET)
	public ResponseEntity<List<DailyFunds>> getDailyFunds(
		@RequestParam(value = "codes") String codes,
		@RequestParam(value = "curdate") String querydate){
		
		String[] codelist=codes.split(",");
		List<DailyFunds> fundslst=dataService.getDailyFunds(codelist, querydate);
		return new ResponseEntity<List<DailyFunds>>(fundslst,HttpStatus.OK);
    }
	
}
