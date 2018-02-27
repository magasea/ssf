package com.shellshellfish.aaas.finance.trade.pay.controller;

import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.pay.service.impl.CheckFundsTradeJobService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.shellshellfish.aaas.finance.trade.pay.model.SellFundResult;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import com.shellshellfish.aaas.finance.trade.pay.service.impl.FundTradeZhongZhengApiService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@RestController
@RequestMapping("/api/trade")
public class TradePayController {
  public static final Logger logger = LoggerFactory.getLogger(TradePayController.class);

  public static String URL_HEAD="/api/userinfo";

  @Autowired
  PayService payService;

	@Autowired
	private CheckFundsTradeJobService jobService;
  
  @Autowired
  FundTradeZhongZhengApiService fundTradeZhongZhengApiService;

  	/**
	 * 赎回理财产品 赎回
	 *
	 * @param tradeAcco
	 * @return
	 */
	@ApiOperation("赎回")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "userPid", dataType = "String", required = true, value = "userPid", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "sellNum", dataType = "Integer", required = true, value = "赎回份额", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "tradeAcco", dataType = "String", required = true, value = "tradeAcco", defaultValue = ""),
	    @ApiImplicitParam(paramType = "query", name = "fundCode", dataType = "String", required = true, value = "基金Code", defaultValue = "") })
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
		@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
		@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@RequestMapping(value = "/funds/sellProduct", method = RequestMethod.GET)
	public ResponseEntity<Map> sellProduct(
			@RequestParam(value = "userPid") String userPid,
			@RequestParam(value = "sellNum") BigDecimal sellNum,

			@RequestParam(value = "tradeAcco") String tradeAcco,
			@RequestParam(value = "fundCode") String fundCode) 
					throws Exception {
		String openId = TradeUtil.getZZOpenId(userPid);
		String outsideOrderNo = UUID.randomUUID().toString();
		Map<String,Object> result = new HashMap<String,Object>();
		SellFundResult sellFund = fundTradeZhongZhengApiService.sellFund(openId, sellNum,
				outsideOrderNo, tradeAcco, fundCode);
		result.put("result", sellFund);
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}
	/**
	 * 日常任务手动触发
	 * 中证支付状态查询更新
	 * @param
	 * @return
	 */
	@ApiOperation("中证支付状态查询更新")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@RequestMapping(value = "/pay/checkjob", method = RequestMethod.GET)
	public ResponseEntity<Map>  jobManualUpdate(){
		Map<String,Object> result = new HashMap<String,Object>();
		jobService.executeSampleJob();
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}
}
