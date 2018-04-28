package com.shellshellfish.aaas.assetallocation.web;

import com.shellshellfish.aaas.assetallocation.service.FundGroupIndexService;
import com.shellshellfish.aaas.assetallocation.service.impl.FundGroupService;
import com.shellshellfish.aaas.assetallocation.util.TradingDayUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * @Author pierre.chen
 * @Date 2018-04-09
 */
@RestController
@RequestMapping("/api/asset-allocation")
public class FundGroupIndexCalculateController {

    @Autowired
    FundGroupIndexService fundGroupIndexService;

    @Autowired
    FundGroupService fundGroupService;

    /**
     * 计算组合历史波动率和历史收益率
     *
     * @return
     */
    @ApiOperation("计算特定组合历史波动率和历史收益率")
    @GetMapping(value = "/calculateAnnualVolatilityAndAnnualYield")
    public HttpStatus calculateAnnualVolatilityAndAnnualYeild(@RequestParam(value = "groupId") String groupId, @RequestParam("subGroupId")
            String subGroupId, @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso =
            DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam(defaultValue = "1") Integer oemId) {
        fundGroupIndexService.calculateAnnualVolatilityAndAnnualYield(groupId, subGroupId, startDate, oemId);
        return HttpStatus.OK;
    }

    /**
     * 计算组合历史波动率和历史收益率
     *
     * @return
     */
    @ApiOperation("计算所有组合历史波动率和历史收益率（每月计算一次）")
    @GetMapping(value = "/calculateAllAnnualVolatilityAndAnnualYield")
    public HttpStatus calculateAllAnnualVolatilityAndAnnualYeild(@RequestParam(value =
            "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate
                                                                         startDate, @RequestParam(defaultValue = "1") Integer oemId) {
        fundGroupIndexService.calculateAnnualVolatilityAndAnnualYield(startDate, oemId);
        return HttpStatus.OK;
    }

    /**
     * 计算组合历史波动率和历史收益率
     *
     * @return
     */
    @ApiOperation("计算所有组合复权单位净值（每日计算）")
    @GetMapping(value = "/calculateGroupNavadj")
    public HttpStatus calculateGroupNavadj(@RequestParam(value = "startDate", required = false)
                                           @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam
                                                   (defaultValue = "1") Integer oemId) {
        if (startDate == null)
            startDate = LocalDate.now(ZoneId.systemDefault());

        if (TradingDayUtils.isTradingDay(startDate)) {
            //只计算交易日
            fundGroupService.calculateGroupNavadj(startDate, oemId);
        }
        return HttpStatus.OK;
    }

    /**
     * 计算组合历史波动率和历史收益率
     *
     * @return
     */
    @ApiOperation("计算所有组合当日最大回撤（每日计算）")
    @GetMapping(value = "/calculateMaxRetracement")
    public HttpStatus calculateMaxRetracement(@RequestParam(value = "date", required = false)
                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                              @RequestParam(defaultValue = "1") Integer oemId) {
        if (date == null)
            date = LocalDate.now(ZoneId.systemDefault());


        if (TradingDayUtils.isTradingDay(date)) {
            //只计算交易日
            fundGroupService.calculateMaxRetracement(date, oemId);
        }
        return HttpStatus.OK;
    }


    /**
     * 计算组合历史波动率和历史收益率
     *
     * @return
     */
    @ApiOperation("计算所有组合组合成立日以来最大回撤")
    @GetMapping(value = "/calculateAllMaxRetracement")
    public HttpStatus calculateMaxRetracement(@RequestParam(defaultValue = "1") Integer oemId) {
        fundGroupService.calculateMaxRetracement(oemId);
        return HttpStatus.OK;
    }

    /**
     * 计算组合历史波动率和历史收益率
     *
     * @return
     */
    @ApiOperation("计算所有组合组合夏普比率")
    @GetMapping(value = "/calculateAllSharpeRatio")
    public HttpStatus calculateAllSharpeRatio(@RequestParam(defaultValue = "1") Integer oemId) {
        fundGroupService.calculateAllSharpeRatio(oemId);
        return HttpStatus.OK;
    }
}


