package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.service.impl.CalculateUserDailyIncome;
import io.swagger.annotations.ApiOperation;

import java.time.LocalDate;
import javax.validation.constraints.NotNull;

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

    @Autowired
    private CalculateUserDailyIncome calculateUserDailyIncome;

    private static final String DEFAULT_DATE_FORMAT_PATTERN = InstantDateUtil.yyyyMMdd;

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

    @ApiOperation("计算用户每日累计收益")
    @GetMapping("/calculateAllUserDailyIncome")
    public HttpStatus calculateDailyIncome() {
        calculateUserDailyIncome.dailyCalculateIncome(LocalDate.now().plusDays(-1));
        return HttpStatus.OK;
    }

    @ApiOperation("计算所有用户区间内每日累计收益")
    @GetMapping("/calculateAllUserDailyIncomeInPeriod")
    public HttpStatus calculateAllUserDailyIncome(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
        LocalDate limitDate = endDate.plusDays(1L);
        while (startDate.isBefore(limitDate)) {
            calculateUserDailyIncome.dailyCalculateIncome(startDate);
            startDate = startDate.plusDays(1);
        }
        return HttpStatus.OK;
    }

    @ApiOperation("计算用户每日累计收益")
    @GetMapping("/calculateUserDailyIncome")
    public HttpStatus calculateDailyIncome(
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
        calculateUserDailyIncome.dailyCalculateIncome(date);
        return HttpStatus.OK;
    }

    @ApiOperation("计算用户每日累计收益!")
    @GetMapping("/calculateUserDailyIncomeInPeriod")
    public HttpStatus calculateUserDailyIncomeInPeroid(
            @RequestParam @NotNull String userUuid,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate endDate) {
        LocalDate limitDate = endDate.plusDays(1L);
        while (startDate.isBefore(limitDate)) {
            calculateUserDailyIncome.calcualteUserDailyIncome(userUuid, startDate);
            startDate = startDate.plusDays(1);
        }
        return HttpStatus.OK;
    }
}
