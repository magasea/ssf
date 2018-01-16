package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.aop.AopLinkResources;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


@RestController
@RequestMapping("/api/userfinance")
public class UserFinanceProdCalController {

    public static final Logger logger = LoggerFactory.getLogger(UserFinanceProdCalController.class);

    @Autowired
    private UserFinanceProdCalcService userFinanceProdCalcService;

    @ApiOperation("每日收益计算")
    @ApiResponses({
            @ApiResponse(code=200,message="OK"),
            @ApiResponse(code=400,message="请求参数没填好"),
            @ApiResponse(code=401,message="未授权用户"),
            @ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
            @ApiResponse(code=404,message="请求路径没有或页面跳")
    })
    @ApiImplicitParams({@ApiImplicitParam()})
    @RequestMapping(value = "/calculate", method = RequestMethod.GET)
    @AopLinkResources
    public ResponseEntity<Object> calculate() throws Exception {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(TradeUtil.getUTCTime1DayBefore());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        userFinanceProdCalcService.dailyCalculation(simpleDateFormat.format(cal.getTime()));
        return new ResponseEntity<Object>(new HashMap<>(), HttpStatus.OK);
    }
}
