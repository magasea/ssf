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
import org.springframework.web.bind.annotation.GetMapping;
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
	@GetMapping("/calculate")
	public HttpStatus calculate() throws Exception {
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(TradeUtil.getUTCTime1DayBefore());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		userFinanceProdCalcService.dailyCalculation(simpleDateFormat.format(cal.getTime()));
		return HttpStatus.OK;
	}
}
