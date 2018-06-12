package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.userinfo.scheduler.CheckCaculateBaseJob;
import com.shellshellfish.aaas.userinfo.service.CaculateUserProdService;
import com.shellshellfish.aaas.userinfo.service.CheckPendingRecordsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by chenwei on 2018- 六月 - 11
 */
@RestController
@RequestMapping("/api/userinfo")
@Api("UserInfo定时任务相关接口")
public class JobController {

  @Autowired
  CaculateUserProdService caculateUserProdService;

  @Autowired
  CheckPendingRecordsService checkPendingRecordsService;

  @ApiOperation("定时计算和更新持仓数量")
  @RequestMapping(value = "/caculateAndUpdate", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<Map> caculateAndUpdate() throws Exception {
    caculateUserProdService.updateCaculateBase();
    return new ResponseEntity<Map>( HttpStatus.OK);
  }

  @ApiOperation("把中证有确认信息的记录恢复到pendingRecords里面")
  @RequestMapping(value = "/patchPendingRecordFromZZInfo", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<Map> patchPendingRecordFromZZInfo() throws Exception {
    checkPendingRecordsService.patchChkPendingRecordFromZZInfo();
    return new ResponseEntity<Map>( HttpStatus.OK);
  }

  @ApiOperation("把pendingRecords中交易状态为确认但是因为navadj值没有取到而处于待处理状态的记录查询到navadj后把状态设置为已处理")
  @RequestMapping(value = "/patchChkPendingRecordNavadj", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<Map> patchChkPendingRecordNavadj() throws Exception {
    checkPendingRecordsService.checkUnhandledRecordWithNavadj();
    return new ResponseEntity<Map>( HttpStatus.OK);
  }
}
