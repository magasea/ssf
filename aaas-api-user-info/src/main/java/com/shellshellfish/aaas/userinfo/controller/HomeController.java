package com.shellshellfish.aaas.userinfo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/api")
public class HomeController {

  @ApiResponse(code = 200, message = "请求成功,获取用户风险偏好和对应产品介绍和明细信息")
  @ApiImplicitParam("用户uuid")

  @RequestMapping(value = "/homepage/{userUuid}", method = RequestMethod.GET)
  public ResponseEntity<?> getHomePage(@PathVariable String userUuid){
    Map<String, Object> result = new HashMap<>();
    if(StringUtils.isEmpty(userUuid)){
      result =  makeDefaultHomePage();
    }else{
      result = makeUserHomePage();
    }
    return new ResponseEntity<Object>(result, HttpStatus.OK);
  }

  private Map<String, Object> makeUserHomePage() {
    return null;
  }

  private Map<String, Object> makeDefaultHomePage() {
    return null;
  }

}
