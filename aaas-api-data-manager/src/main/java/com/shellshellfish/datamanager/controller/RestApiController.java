package com.shellshellfish.datamanager.controller;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SimpleTimeZone;

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
import com.shellshellfish.datamanager.model.FundManagers;
import com.shellshellfish.datamanager.model.IndicatorPoint;
import com.shellshellfish.datamanager.service.DataService;
import com.shellshellfish.datamanager.service.DataServiceImpl;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Configuration

@RestController
@RequestMapping("api/datamanager")
@Validated
@Api("基金数据管理相关restapi")
public class RestApiController {
  //public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	private static final Class<?> UserException = null;

	@Autowired
	DataService dataService;
	
	///@Value("${daily.fromdate}")
    String deffromdate;
    
	//@Value("${daily.todate}")
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
		List<DailyFunds> fundslst=dataService.getDailyFundsBycode(codelist);
		return new ResponseEntity<List<DailyFunds>>(fundslst,HttpStatus.OK);
    }
	
	//最大回撤区间函数:(得到此区间内所有复权单位净值)
	@RequestMapping(value = "/getMaxfallPeriod", method = RequestMethod.GET)
	public ResponseEntity<List<IndicatorPoint>> getMaxFallPeriod(
			@RequestParam(value = "code") String code,
			@RequestParam(value = "fromdate") String fromdate,
			@RequestParam(value = "todate") String todate){
		
		Date stdate=null;
		Date enddate=null;
		long sttime=0L;
		long endtime=0L;
		Timestamp ts;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			stdate = sdf.parse(fromdate);
			
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(stdate);//date 换成已经已知的Date对象
	        cal.add(Calendar.HOUR_OF_DAY, -8);// before 8 hour (GMT 8)
	        Date e=cal.getTime();
	        sttime=e.getTime()/1000;
	        
	        enddate= sdf.parse(todate);
	        
	        cal.setTime(enddate);
	        cal.add(Calendar.HOUR_OF_DAY, -8);// before 8 hour (GMT 8)
	        e=cal.getTime();
	        endtime=e.getTime()/1000;
	        
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			return new ResponseEntity<List<IndicatorPoint>>(new ArrayList<IndicatorPoint>(),HttpStatus.BAD_REQUEST);
		}	
		
		List<IndicatorPoint> lst=dataService.getMaxfallPeriod(code, sttime, endtime);
		return new ResponseEntity<List<IndicatorPoint>>(lst,HttpStatus.OK);
	   
	}
	
	//基金经理
	@RequestMapping(value = "/getFundManager", method = RequestMethod.GET)
	public ResponseEntity<HashMap<String,Object>> getDailyFunds(
		@RequestParam(value = "name") String name){
		
		
		
		HashMap fundmanagersmap=dataService.getFundManager(name);
		
		return new ResponseEntity<HashMap<String,Object>>(fundmanagersmap,HttpStatus.OK);
    }
	
	//基金概况
	@RequestMapping(value = "/getFundInfo", method = RequestMethod.GET)
	public ResponseEntity<List<DailyFunds>> getFundInfo(
		@RequestParam(value = "codes") String codes){
		
		String[] codelist=codes.split(",");
		List<DailyFunds> fundslst=dataService.getDailyFundsBycode(codelist);
		return new ResponseEntity<List<DailyFunds>>(fundslst,HttpStatus.OK);

    }
	
	
	//基金经理
	@RequestMapping(value = "/getFundCompany", method = RequestMethod.GET)
	public ResponseEntity<HashMap<String,Object>> getFundCompany(
			@RequestParam(value = "name") String name){
			
			
			
			HashMap fundmanagersmap=dataService.getFundCompany(name);
			
			return new ResponseEntity<HashMap<String,Object>>(fundmanagersmap,HttpStatus.OK);
	 }
		
		
	//历史净值
	//code:基金代码
	//period: 1: 3month,2: 6month,3: 1year,4: 3year
	@RequestMapping(value = "/getHistoryNetvalue", method = RequestMethod.GET)
	public ResponseEntity<HashMap<String,Object>> getHistoryNetvalue(
			@RequestParam(value = "code") String code,
			@RequestParam(value = "period") String period){
    			
		   HashMap hnmap=dataService.getHistoryNetvalue(code,period);
		   return new ResponseEntity<HashMap<String,Object>>(hnmap,HttpStatus.OK);
	}
	
	
	 //基金概况
	 @RequestMapping(value = "/getFundValueInfo", method = RequestMethod.GET)
	 public ResponseEntity<HashMap<String,Object>> getFundValueInfo(
			@RequestParam(value = "code") String code){
			
			HashMap valmap=dataService.getFundValueInfo(code);
			return new ResponseEntity<HashMap<String,Object>>(valmap,HttpStatus.OK);

	 }
		

}
