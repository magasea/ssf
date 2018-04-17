package com.shellshellfish.aaas.assetallocation.web;

import com.shellshellfish.aaas.assetallocation.job.service.JobScheduleService;
import com.shellshellfish.aaas.assetallocation.returnType.JobResult;
import com.shellshellfish.aaas.assetallocation.service.impl.FundGroupService;
import com.shellshellfish.aaas.assetallocation.util.ThreadPoolUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutorService;

/**
 * Author: hongming
 * Date: 2018/1/20
 * Desc:
 */
@RestController
public class JobScheduleController {

    @Autowired
    JobScheduleService jobScheduleService;


    @Autowired
    FundGroupService fundGroupService;

    /**
     * 每日接口获取数据定时任务
     */
    @ApiOperation("每日接口获取数据定时任务")
    @RequestMapping(value = "/api/asset-allocation/job/insertDailyFund", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobResult insertDailyFund() {
        jobScheduleService.insertDailyFundJobSchedule();
        return new JobResult<>().returnSuccess();
    }

    /**
     * 计算每周收益率以及风险率数据
     */
    @ApiOperation("计算每周收益率以及风险率数据")
    @RequestMapping(value = "/api/asset-allocation/job/calculateYieldAndRiskOfWeek", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobResult calculateYieldAndRiskOfWeek() {
        jobScheduleService.calculateYieldAndRiskOfWeekJobSchedule();
        return new JobResult<>().returnSuccess();
    }

    /**
     * 计算产品组合数据(产品组合风险率、收益率、权重)
     */
    @ApiOperation("计算产品组合数据(产品组合风险率、收益率、权重)")
    @RequestMapping(value = "/api/asset-allocation/job/insertFundGroupData", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobResult insertFundGroupData() {
        jobScheduleService.insertFundGroupDataJobSchedule();
        return new JobResult<>().returnSuccess();
    }

    /**
     * 计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比，运行时间较长
     */
    @ApiOperation("计算 单位收益净值、最大回撤、夏普比率、基金收益贡献比，运行时间较长")
    @RequestMapping(value = "/api/asset-allocation/job/getAllIdAndSubId", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobResult getAllIdAndSubId(@RequestParam(defaultValue = "1") Integer oemId) {
        if(oemId < 0 || oemId > 1000){
            throw new IllegalArgumentException("oemId:"+ oemId);
        }
        jobScheduleService.getAllIdAndSubIdJobSchedule(oemId);
        return new JobResult<>().returnSuccess();
    }

    /**
     * 更新所有基金组合的最大亏损额
     */
    @ApiOperation("更新所有基金组合的最大亏损额")
    @RequestMapping(value = "/api/asset-allocation/job/updateAllMaximumLosses/", method =
        RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobResult updateAllMaximumLosses(@RequestParam(defaultValue = "1") Integer oemId) {
        if(oemId < 0 || oemId > 1000){
            throw new IllegalArgumentException("oemId:"+oemId);
        }
        ExecutorService pool = ThreadPoolUtil.getThreadPool();
        pool.execute(() -> {
            jobScheduleService.updateAllMaximumLossesJobSchedule(oemId);
        });
        return new JobResult<>().returnSuccess();
    }

    /**
     * 组合收益率(最大回撤)走势图-自组合基金成立以来的每天
     */
    @ApiOperation("组合收益率(最大回撤)走势图-自组合基金成立以来的每天")
    @RequestMapping(value = "/api/asset-allocation/job/getFundGroupIncomeAllToMongoDb", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobResult getFundGroupIncomeAllToMongoDb(@RequestParam(defaultValue = "1") Integer
        oemId) {
        jobScheduleService.getFundGroupIncomeAllJobSchedule(oemId);
        return new JobResult<>().returnSuccess();
    }


}
