package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.userinfo.service.CheckUiProductDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author pierre.chen
 * @Date 18-5-17
 */
@RestController
@RequestMapping(value = "/api/riskassessments")
public class CheckUiProductDetailController {

    @Autowired
    CheckUiProductDetailService checkUiProductDetailService;

    @ApiOperation("校验用户基金份额")
    @RequestMapping(value = "/checkUiProductDetail", method = RequestMethod.POST)
    public HttpStatus CheckUiProductDetail() {
        checkUiProductDetailService.check();
        return HttpStatus.OK;
    }

}
