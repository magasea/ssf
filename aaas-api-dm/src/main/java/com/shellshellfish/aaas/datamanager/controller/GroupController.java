package com.shellshellfish.aaas.datamanager.controller;

import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.datamanager.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

/**
 * @Author pierre 18-3-12
 */
@Api("组合相关接口")
@RestController
@RequestMapping("/api/datamanager/")
public class GroupController {


    @Qualifier("dataServiceImpl")
    @Autowired
    DataService dataService;


    @ApiOperation("获取组合基准数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "groupId", dataType = "long", required = true, value = "组合ID", defaultValue = "1"),
            @ApiImplicitParam(name = "endDate", value = "截止日期", paramType = "query", dataType = "String", defaultValue = "yyyy-MM-dd"),
            @ApiImplicitParam(name = "period", value = "区间 1:3个月,2:6个月 3:一年 4:三年 5: 成立以来所有", paramType = "query", dataType = "int", defaultValue = "1")
    })
    @GetMapping(value = "/getGroupBaseLine")
    public Map<String, Object> getGroupBaseLine(
            @NotNull(message = "组合ID不能为空") @RequestParam(value = "groupId") Long groupId,
            @RequestParam(value = "endDate", required = false) String endDateStr,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "period", required = false) Integer period) {
        LocalDate endDate;
        if (StringUtils.isEmpty(endDateStr)) {
            endDate = LocalDate.now();
        } else {
            endDate = InstantDateUtil.format(endDateStr);
        }
        endDate = Optional.ofNullable(endDate).orElse(LocalDate.now());
        LocalDate startLocalDate = endDate;
        switch (period) {
            case 1:
                startLocalDate = endDate.plusMonths(-3);
                break;
            case 2:
                startLocalDate = endDate.plusMonths(-6);
                break;
            case 3:
                startLocalDate = endDate.plusYears(-1);
                break;
            case 4:
                startLocalDate = endDate.plusYears(-3);
                break;
            case 5:
                startLocalDate = LocalDate.MIN;
                break;
            default:
                startLocalDate = LocalDate.MIN;
        }
        if (startDate != null)
            startLocalDate = startDate.plusDays(-1);

        return dataService
                .getGroupBaseLine(groupId, InstantDateUtil.getEpochSecondOfZero(startLocalDate),
                        InstantDateUtil.getEpochSecondOfZero(endDate.plusDays(1)));

    }
    //组合基准
}
