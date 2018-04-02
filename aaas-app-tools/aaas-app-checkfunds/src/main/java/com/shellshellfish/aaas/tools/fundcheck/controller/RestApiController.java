package com.shellshellfish.aaas.tools.fundcheck.controller;


import com.shellshellfish.aaas.common.http.HttpJsonResult;
import com.shellshellfish.aaas.tools.fundcheck.model.BaseCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCheckRecord;
import com.shellshellfish.aaas.tools.fundcheck.model.FundCodes;
import com.shellshellfish.aaas.tools.fundcheck.service.CsvFundInfoService;
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

	@Autowired
	private CsvFundInfoService csvFundInfoService;



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
	public HttpJsonResult syncChoice()  {
		HttpJsonResult result = new HttpJsonResult();
		try {
			makeSequenceInitCalls();
		} catch (Exception e) {
			e.printStackTrace();
			return HttpJsonResult.returnFail(e.getMessage());
		}

		return HttpJsonResult.returnSuccess();
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

	private boolean makeSequenceInitCalls() throws Exception {
		csvFundInfoService.restApiCall();
		return true;
	}

}
