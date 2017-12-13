package com.shellshellfish.aaas.finance.trade.order.controller;

import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
public class TradeOrderController {
  public static final Logger logger = LoggerFactory.getLogger(TradeOrderController.class);

  public static String URL_HEAD="/api/userinfo";

  @Autowired
  TradeOpService tradeOpService;



  /**
   * 购买理财产品 页面
   *
   * @param id
   * @return
   */
  @ApiOperation("购买理财产品 页面")
  @ApiResponses({
      @ApiResponse(code=200,message="OK"),
      @ApiResponse(code=400,message="请求参数没填好"),
      @ApiResponse(code=401,message="未授权用户"),
      @ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
      @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
  })
  @ApiImplicitParam(paramType="path",name="Uuid",dataType="String",required=true,value="用户Uuid",defaultValue="")
  @RequestMapping(value = "/funds/buy}", method = RequestMethod.GET)
  public ResponseEntity<?> buyFinanceProd(
      @Valid @NotNull(message = "用户Uuid不能为空") @RequestParam("Uuid") String userUuid, @Valid
  @NotNull(message = "理财产品id 不能为空") @RequestParam("prodId") String prodId)
      throws Exception {
    if(StringUtils.isEmpty(userUuid)){
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }else{

    }
  }



}
