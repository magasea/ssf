package com.shellshellfish.aaas.datamanager.controller;

import com.shellshellfish.aaas.datamanager.service.impl.MongoFundBaseCloseCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author pierre.chen
 * @Date 18-6-2
 */
@Api("checkFundBaseClose")
@RestController
public class DemoController {

    @Autowired
    MongoFundBaseCloseCheck mongoFundBaseCloseCheck;

    @ApiOperation("checkFundBaseClose")
    @GetMapping(value = "/checkFundBaseClose")
    public void check() {
        mongoFundBaseCloseCheck.check();
    }
}
