package com.shellshellfish.aaas.tools.fundcheck.controller;


import com.shellshellfish.aaas.common.http.HttpJsonResult;
import com.shellshellfish.aaas.tools.fundcheck.model.BaseCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCodes;
import com.shellshellfish.aaas.tools.fundcheck.service.DataService;
import com.shellshellfish.aaas.tools.fundcheck.service.FundUpdateJobService;
import com.shellshellfish.aaas.tools.fundcheck.service.impl.DataServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Configuration

@RestController
@RequestMapping("/api/fundcheck")
@Validated
@Api("fundcheck summary")
public class RestApiController {
	public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	final static String CNST_BASE = "CLOSE";

	@Autowired
	private FundUpdateJobService fundUpdateJobService;

	@Value("${shellshellfish.asset-allocation-insertdf-url}")
	String assetAllocationInsertdf;

	@Value("${shellshellfish.asset-allocation-inithistory-url}")
	String assetAllocationInithistory;

	@Value("${shellshellfish.asset-allocation-initpyamongo-url}")
	String assetAllocationInitpyamongo;

	@Value("${shellshellfish.data-manager-initcache-url}")
	String assetAllocationInitcache;

	@Value("${shellshellfish.data-manager-initcache-detail-url}")
	String assetAllocationInitcacheDetail;


	@GetMapping("/statusBase")
	public List<BaseCheckRecord> statusBase() {

//        List<FundCheckRecord> fundCheckRecordList =         fundUpdateJobService
//            .getCurrentFundCheckRecord();
		List<BaseCheckRecord> baseCheckRecordList =  fundUpdateJobService
				.getBaseFundCheckRecord();
//        JsonResult jsonResult = new JsonResult();
//        List<Object> totalList = new ArrayList<>();
//        CollectionUtils.mergeArrayIntoCollection(fundCheckRecordList, totalList);
//        CollectionUtils.mergeArrayIntoCollection(baseCheckRecordList, totalList);
//        jsonResult.setResult(totalList);
		return baseCheckRecordList;

	}

	@GetMapping("/syncChoice") // //new annotation since 4.3
	public String syncChoice() throws Exception {


		makeSequenceInitCalls();

		return "redirect:/uploadStatus";
	}

	@GetMapping("statusFund")
	public List<FundCheckRecord> statusFund() {

//        List<FundCheckRecord> fundCheckRecordList =         fundUpdateJobService
//            .getCurrentFundCheckRecord();
		List<FundCheckRecord> fundCheckRecordList =  fundUpdateJobService
				.getCurrentFundCheckRecord();
//        JsonResult jsonResult = new JsonResult();
//        List<Object> totalList = new ArrayList<>();
//        CollectionUtils.mergeArrayIntoCollection(fundCheckRecordList, totalList);
//        CollectionUtils.mergeArrayIntoCollection(baseCheckRecordList, totalList);
//        jsonResult.setResult(totalList);
		return fundCheckRecordList;

	}

	private boolean makeSequenceInitCalls(){
		RestTemplate restTemplate = new RestTemplate();
		try{
			HttpJsonResult jsonResult1 = restTemplate.getForObject(assetAllocationInsertdf,
					HttpJsonResult.class);
			Thread.sleep(10);
			if(jsonResult1 != null){
				logger.info(jsonResult1.toString());
			}
			HttpJsonResult jsonResult2 = restTemplate.getForObject(assetAllocationInithistory,
					HttpJsonResult.class);
			Thread.sleep(10);
			if(jsonResult2 != null){
				logger.info(jsonResult2.toString());
			}
			HttpJsonResult jsonResult3 = restTemplate.getForObject(assetAllocationInitpyamongo
					, HttpJsonResult.class);
			Thread.sleep(10);
			if(jsonResult3 != null){
				logger.info(jsonResult3.toString());
			}
			HttpJsonResult jsonResult4 = restTemplate.getForObject(assetAllocationInitcache, HttpJsonResult.class);
			Thread.sleep(10);
			if(jsonResult4 != null){
				logger.info(jsonResult4.toString());
			}
			HttpJsonResult jsonResult5 = restTemplate.getForObject
					(assetAllocationInitcacheDetail, HttpJsonResult.class);
			Thread.sleep(10);
			if(jsonResult5 != null){
				logger.info(jsonResult5.toString());
			}

		}catch (Exception ex){
			logger.error("Exception:", ex);
		}

		return true;
	}

}
