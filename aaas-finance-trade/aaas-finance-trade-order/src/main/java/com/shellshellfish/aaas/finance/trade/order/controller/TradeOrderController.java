package com.shellshellfish.aaas.finance.trade.order.controller;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;

import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.service.TradeSellService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
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
  
  @Autowired
  OrderService orderService;

	@Autowired
	TradeSellService tradeSellService;



  /**
   * 购买理财产品 页面
   *
   * @param financeProdBuyInfo
   * @return
   */
  @ApiOperation("理财购买")
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
    TrdOrder trdOrder = tradeOpService.buyFinanceProduct(financeProdBuyInfo);
    return new ResponseEntity<Object>(trdOrder, HttpStatus.OK);
  }


	/**
	 * 赎回理财产品 页面
	 *
	 * @param prodSellPageDTO
	 * @return
	 */
	@ApiOperation("理财赎回")
	@ApiResponses({
			@ApiResponse(code=200,message="OK"),
			@ApiResponse(code=400,message="请求参数没填好"),
			@ApiResponse(code=401,message="未授权用户"),
			@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/funds/sell", method = RequestMethod.POST)
	public ResponseEntity<?> buyFinanceProd(@RequestBody ProdSellPageDTO prodSellPageDTO)
			throws Exception {
		if( prodSellPageDTO.getUserId() == 0L){
			logger.info("input userId is empty, need retrieve userId");
			Long userId = tradeOpService.getUserId(prodSellPageDTO.getUserUuid());
			prodSellPageDTO.setUserId(userId);
		}
//		Long userId = tradeOpService.getUserId(financeProdBuyInfo.getUuid());
//		financeProdBuyInfo.setUserId(userId);
		TrdOrder trdOrder = tradeSellService.sellProduct(prodSellPageDTO);
		return new ResponseEntity<Object>(trdOrder, HttpStatus.OK);
	}
  
  
  
  /**
   * 购买理财产品 购买
   *
   * @param totalAmount
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
		//最大金额最小金额判断
		boolean result = financeProdCalcService.getMaxMinResult(productList, totalAmount);
		if(!result){
			throw new Exception("输入金额数不在最大最小限制内，请重新输入");
		}
		DistributionResult distributionResult = financeProdCalcService.getPoundageOfBuyFund(totalAmount, productList);
		return new ResponseEntity<DistributionResult>(distributionResult, HttpStatus.OK);
	} 
	
	/**
	 * 赎回理财产品 赎回
	 *
	 * @param totalAmount
	 * @return
	 */
	@ApiOperation("赎回理财产品 赎回页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "Long", required = true, value = "groupId", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "Long", required = true, value = "subGroupId", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "totalAmount", dataType = "BigDecimal", required = true, value = "赎回金额", defaultValue = "") })
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
		@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
		@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@RequestMapping(value = "/funds/sellProduct", method = RequestMethod.GET)
	public ResponseEntity<DistributionResult> sellProduct(
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

	/**
	   * 购买理财产品 产品详情
	   *
	   * @param totalAmount
	   * @return
	   */
	@ApiOperation("购买理财产品 产品详情页面")
	@ApiImplicitParams({
//				@ApiImplicitParam(paramType = "path", name = "uuid", dataType = "String", required = true, value = "用户UUID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "orderId", dataType = "String", required = true, value = "订单编号", defaultValue = "1231230001000001513657092497")
	})
	@ApiResponses({ 
			@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@RequestMapping(value = "/funds/buyDetails/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Map> buyDetails(
			// @PathVariable(value = "groupId") Long uuid,
			@PathVariable(value = "orderId") String orderId) throws Exception {
		logger.error("method buyDetails run ..");
		Map<String, Object> result = new HashMap<String, Object>();
		if(StringUtils.isEmpty(orderId)){
			logger.error("购买详情不存在:"+orderId);
			throw new Exception("购买详情不存在:"+orderId);
		}
		TrdOrder trdOrder = orderService.getOrderByOrderId(orderId);
		List<TrdOrderDetail> trdOrderDetailList = new ArrayList<TrdOrderDetail>();
		if (trdOrder != null && trdOrder.getOrderId() != null) {
			trdOrderDetailList = orderService.findOrderDetailByOrderId(orderId);
		} else {
			logger.error("购买详情不存在.");
			throw new Exception("购买详情不存在.");
		}
		//result.put("list", trdOrderDetailList);
		if(trdOrderDetailList==null||trdOrderDetailList.isEmpty()){
			logger.error("购买详情信息数据不存在.");
			throw new Exception("购买详情信息数据不存在.");
		}
		Map<String,Object> trdOrderMap = new HashMap<String,Object>();
		if(trdOrder.getOrderStatus() == 0){
			result.put("orderStatus", "待确认");
		} else if(trdOrder.getOrderStatus() == 1){
			result.put("orderStatus", "已确认");
		} else {
			logger.info("状态为："+trdOrder.getOrderStatus());
			result.put("orderStatus", "交易失败");
		}
		if(trdOrder.getOrderType() == 1){
			result.put("orderType", "购买");
		} else if(trdOrder.getOrderType() == 2){
			result.put("orderType", "分红");
		} else {
			logger.info("状态为："+trdOrder.getOrderType());
			result.put("orderType", "交易失败");
		}
		
		//TODO title
		result.put("title", "稳健型-3个月组合");
		//金额
		result.put("amount", trdOrder.getPayAmount());
		//手续费
		result.put("payfee", trdOrder.getPayFee());
		Calendar c = Calendar.getInstance();
		List<Map<String,Object>> statusList = new ArrayList<Map<String,Object>>();
		Map<String,Object> statusMap = new HashMap<String,Object>();
		statusMap.put("time", c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE));
		statusMap.put("date", c.get(Calendar.YEAR)+"."+c.get(Calendar.MONTH)+"."+c.get(Calendar.DATE));
		statusMap.put("status", "申请已受理");
		statusList.add(statusMap);
		
		statusMap = new HashMap<String,Object>();
		statusMap.put("time", c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE));
		statusMap.put("date", c.get(Calendar.YEAR)+"."+c.get(Calendar.MONTH)+"."+(c.get(Calendar.DATE)+2));
		statusMap.put("status", "份额确认，开始计算收益");
		statusList.add(statusMap);
		
		statusMap = new HashMap<String,Object>();
		statusMap.put("time", c.get(Calendar.HOUR_OF_DAY)+":"+c.get(Calendar.MINUTE));
		statusMap.put("date", c.get(Calendar.YEAR)+"."+c.get(Calendar.MONTH)+"."+(c.get(Calendar.DATE)+3));
		statusMap.put("status", "查看收益");
		statusList.add(statusMap);
		
		result.put("statusList", statusList);
		
		//状态详情
		List<Map<String,Object>> detailList = new ArrayList<Map<String,Object>>();
		Map<String,Object> detailMap = new HashMap<String,Object>();
		for(int i=0;i<trdOrderDetailList.size();i++){
			detailMap = new HashMap<String,Object>();
			TrdOrderDetail trdOrderDetail = trdOrderDetailList.get(i);
			if(trdOrderDetail.getOrderDetailStatus()==0){
				detailMap.put("status", "待确认");
			} else if(trdOrderDetail.getOrderDetailStatus()==1){
				detailMap.put("fundstatus", "已确认");
			} else {
				detailMap.put("fundstatus", "交易失败");
			}
			detailMap.put("fundCode", trdOrderDetail.getFundCode());
			//基金费用
			detailMap.put("fundbuyFee", trdOrderDetail.getBuyFee());
			detailMap.put("funddate", c.get(Calendar.YEAR)+"."+c.get(Calendar.MONTH)+"."+c.get(Calendar.DATE));
			if(trdOrderDetail.getTradeType() == 1){
				detailMap.put("fundTradeType", "购买");
			} else if(trdOrderDetail.getTradeType() == 2){
				detailMap.put("fundTradeType", "分红");
			} else if(trdOrderDetail.getTradeType() == 9){
				detailMap.put("fundTradeType", "部分确认");
			} else {
				logger.info("状态为："+trdOrder.getOrderType());
				detailMap.put("fundTradeType", "交易失败");
			}
			detailList.add(detailMap);
		}
		result.put("detailList", detailList);
		
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}


	/**
	 * 购买理财产品 页面
	 *
	 * @param financeProdBuyInfo
	 * @return
	 */
	@ApiOperation("理财购买")
	@ApiResponses({
			@ApiResponse(code=200,message="OK"),
			@ApiResponse(code=400,message="请求参数没填好"),
			@ApiResponse(code=401,message="未授权用户"),
			@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/funds/buywithpreorder", method = RequestMethod.POST)
	public ResponseEntity<?> buyFinanceProdWithPreOrder(@RequestBody FinanceProdBuyInfo
			financeProdBuyInfo)
			throws Exception {
		Long userId = null ;
		if( null == financeProdBuyInfo.getUserId()){
			logger.info("input userId is empty, need retrieve userId");
			userId = tradeOpService.getUserId(financeProdBuyInfo.getUuid());
			financeProdBuyInfo.setUserId(userId);
		}
		if(null == userId){
			logger.error("cannot find userId for user:"+ financeProdBuyInfo.getUuid());
			return new ResponseEntity<Object>("cannot find userId for user:"+ financeProdBuyInfo
					.getUuid(), HttpStatus.NOT_ACCEPTABLE);
		}
		financeProdBuyInfo.setUserId(userId);
		financeProdBuyInfo.setMoney(financeProdBuyInfo.getMoney());
		TrdOrder trdOrder = tradeOpService.buyFinanceProductWithPreOrder(financeProdBuyInfo);
		return new ResponseEntity<Object>(trdOrder, HttpStatus.OK);
	}
	
	/**
	 * 获取最大值最小值
	 *
	 * @param totalAmount
	 * @return
	 */
	@ApiOperation("获取购买的最大值最小值")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "Long", required = true, value = "groupId", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "Long", required = true, value = "subGroupId", defaultValue = "")
		})
	@ApiResponses({ 
		@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
		@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
		@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@RequestMapping(value = "/funds/maxminValue", method = RequestMethod.GET)
	public ResponseEntity<Map> getMaxMinValue(
			@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "subGroupId") Long subGroupId)
					throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ProductBaseInfo productBaseInfo = new ProductBaseInfo();
		productBaseInfo.setProdId(groupId);
		productBaseInfo.setGroupId(subGroupId);
		List<ProductMakeUpInfo> productList = financeProdInfoService.getFinanceProdMakeUpInfo(productBaseInfo);
		BigDecimal min = financeProdCalcService.getMinBuyAmount(productList);
		BigDecimal max = financeProdCalcService.getMaxBuyAmount(productList);
		result.put("min", min);
		result.put("max", max);
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	} 

}
