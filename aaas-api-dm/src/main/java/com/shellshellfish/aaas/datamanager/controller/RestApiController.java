package com.shellshellfish.aaas.datamanager.controller;


import com.shellshellfish.aaas.datamanager.model.FundCodes;
import com.shellshellfish.aaas.datamanager.service.DataService;
import com.shellshellfish.aaas.datamanager.service.impl.DataServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping(value = "/getAllFundCodes", method = RequestMethod.GET)
    public ResponseEntity<List<FundCodes>> getAllFundCodes() {

        List<FundCodes> fundslst = dataService.getAllFundCodes();
        return new ResponseEntity<List<FundCodes>>(fundslst, HttpStatus.OK);
    }


    //基金经理
    @ApiOperation("基金经理信息")
    @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "基金经理名字", defaultValue = "")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "请求参数没填好"),

    })

    @RequestMapping(value = "/getFundManager", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getDailyFunds(
            @RequestParam(value = "name") String name) {

        HashMap<String, Object> fundmanagersmap = dataService.getFundManager(name);
        if (fundmanagersmap == null) {
            HashMap<String, Object> errorMap = new HashMap();
            errorMap.put("error msg", "该基金经理不存在");
            return new ResponseEntity<HashMap<String, Object>>(errorMap, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<HashMap<String, Object>>(fundmanagersmap, HttpStatus.OK);
    }


    //根据基金code查基金相关信息，包括公司，经理
    @ApiOperation("基金概况")
    @ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "基金代码", defaultValue = "")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "请求参数没填好")

    })
    @RequestMapping(value = "/getFundInfoBycode", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getFundInfoBycode(
            @RequestParam(value = "code") String code) {

        HashMap fundmanagersmap = dataService.getFundInfoBycode(code);

        return new ResponseEntity<HashMap<String, Object>>(fundmanagersmap, HttpStatus.OK);
    }


    //基金公司
    @ApiOperation("基金公司")
    @ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "基金名称", defaultValue = "")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "请求参数没填好")

    })
    @RequestMapping(value = "/getFundCompanyDetailInfo", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getFundCompanyDetailInfo(
            @RequestParam(value = "name") String name) {

        HashMap fundmanagersmap = dataService.getFundCompanyDetailInfo(name);

        return new ResponseEntity<HashMap<String, Object>>(fundmanagersmap, HttpStatus.OK);
    }


    //历史净值
    @ApiOperation("历史净值")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "基金代码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "date", value = "日期", required = false, paramType = "query", dataType = "String")
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "请求参数没填好")

    })
    //历史净值
    //type: 1: 3month,2: 6month,3: 1year,4: 3year
    @RequestMapping(value = "/getHistoryNetvalue", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getHistoryNetvalue(
            @NotNull(message = "代码不能为空") @RequestParam(value = "code") String code,
            @NotNull(message = "类型不能为空") @RequestParam(value = "type") String type,
            @RequestParam(value = "date", required = false) String date) {

        if (date == null) {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        HashMap hnmap = dataService.getHistoryNetvalue(code, type, date);
        return new ResponseEntity<HashMap<String, Object>>(hnmap, HttpStatus.OK);
    }


    //日涨幅,近一年涨幅,净值,分级类型,评级
    @RequestMapping(value = "/getFundValueInfo", method = RequestMethod.GET)
    public ResponseEntity<HashMap<String, Object>> getFundValueInfo(
            @NotNull(message = "代码不能为空") @RequestParam(value = "code") String code,
            @RequestParam(value = "date", required = false) String date) {

        if (date == null) {
            date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        HashMap valmap = dataService.getFundValueInfo(code, date);
        return new ResponseEntity<HashMap<String, Object>>(valmap, HttpStatus.OK);

    }


}
