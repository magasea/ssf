package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import io.swagger.annotations.ApiOperation;
import java.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/userfinance")
public class UserFinanceProdCalController {

	public static final Logger logger = LoggerFactory.getLogger(UserFinanceProdCalController.class);

	@Autowired
	private UserFinanceProdCalcService userFinanceProdCalcService;

	private static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyyMMdd";

	@ApiOperation("每日收益计算")
	@GetMapping("/calculate")
	public HttpStatus calculateDaily() throws Exception {
		String today = InstantDateUtil
				.format(LocalDate.now().plusDays(-1), DEFAULT_DATE_FORMAT_PATTERN);
		userFinanceProdCalcService.dailyCalculation(today);
		//TODO: need to fix it because it always return OK
		return HttpStatus.OK;
	}

	@ApiOperation("每日收益计算")
	@GetMapping("/calculateDays")
	public HttpStatus calculate(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) throws Exception {
		LocalDate limitDate = endDate.plusDays(1L);
		while (startDate.isBefore(limitDate)) {
			userFinanceProdCalcService
					.dailyCalculation(InstantDateUtil.format(startDate, DEFAULT_DATE_FORMAT_PATTERN));
			startDate = startDate.plusDays(1);
		}
		return HttpStatus.OK;
	}

}
