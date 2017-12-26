package com.shellshellfish.aaas.finance.trade.order.controller;

import java.math.BigDecimal;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.finance.trade.order.model.DistributionResult;
import com.shellshellfish.aaas.finance.trade.order.model.vo.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdCalcService;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
import com.shellshellfish.aaas.finance.trade.order.service.TradeOpService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/trade")
public class TradeOrderController {
  public static final Logger logger = LoggerFactory.getLogger(TradeOrderController.class);

  public static String URL_HEAD="/api/userinfo";

  @Autowired
  TradeOpService tradeOpService;
  
  @Autowired
  FinanceProdInfoService financeProdInfoService;
  
  @Autowired
  FinanceProdCalcService financeProdCalcService;



  /**
   * 购买理财产品 页面
   *
   * @param financeProdBuyInfo
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
  @RequestMapping(value = "/funds/buy", method = RequestMethod.POST)
  public ResponseEntity<?> buyFinanceProd(@RequestBody FinanceProdBuyInfo financeProdBuyInfo)
      throws Exception {
    if( null == financeProdBuyInfo.getUserId()){
      logger.info("input userId is empty, need retrieve userId");
      Long userId = tradeOpService.getUserId(financeProdBuyInfo.getUuid());
      financeProdBuyInfo.setUserId(userId);
    }
    Long userId = tradeOpService.getUserId(financeProdBuyInfo.getUuid());
    financeProdBuyInfo.setUserId(userId);
    financeProdBuyInfo.setMoney(financeProdBuyInfo.getMoney());
    tradeOpService.buyFinanceProduct(financeProdBuyInfo);
    return new ResponseEntity<Object>(HttpStatus.OK);
  }
  
  /**
   * 购买理财产品 购买
   *
   * @param financeProdBuyInfo
   * @return
   */
	@ApiOperation("购买理财产品 购买页面")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "Long", required = true, value = "groupId", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "Long", required = true, value = "subGroupId", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "totalAmount", dataType = "BigDecimal", required = true, value = "购买金额", defaultValue = "") })
	@ApiResponses({ 
			@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@RequestMapping(value = "/funds/buyProduct", method = RequestMethod.GET)
	public ResponseEntity<DistributionResult> buyProduct(
			@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "subGroupId") Long subGroupId,
			@RequestParam(value = "totalAmount") BigDecimal totalAmount)
			throws Exception {
		ProductBaseInfo productBaseInfo = new ProductBaseInfo();
		productBaseInfo.setProdId(groupId);
		productBaseInfo.setGroupId(subGroupId);
		List<ProductMakeUpInfo> productList = financeProdInfoService.getFinanceProdMakeUpInfo(productBaseInfo);
		DistributionResult distributionResult = financeProdCalcService.getPoundageOfSellFund(totalAmount, productList);
		return new ResponseEntity<DistributionResult>(distributionResult, HttpStatus.OK);
	} 



}
