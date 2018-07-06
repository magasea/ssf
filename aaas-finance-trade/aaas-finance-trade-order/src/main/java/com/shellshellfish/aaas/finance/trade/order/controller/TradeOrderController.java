package com.shellshellfish.aaas.finance.trade.order.controller;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.finance.trade.order.model.DistributionResult;
import com.shellshellfish.aaas.finance.trade.order.model.FundAmount;
import com.shellshellfish.aaas.finance.trade.order.model.FundDetailResult;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.model.vo.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPageDTO;
import com.shellshellfish.aaas.finance.trade.order.model.vo.ProdSellPercentDTO;
import com.shellshellfish.aaas.finance.trade.order.service.*;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/trade")
public class TradeOrderController {
	public static final Logger logger = LoggerFactory.getLogger(TradeOrderController.class);

	public static String URL_HEAD = "/api/userinfo";

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
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"),
			@ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/funds/buy", method = RequestMethod.POST)
	public ResponseEntity<?> buyFinanceProd(@RequestBody FinanceProdBuyInfo financeProdBuyInfo)
			throws Exception {
		UserInfo userInfo = tradeOpService.getUserInfoByUserUUID(financeProdBuyInfo.getUuid());
		Long userId = userInfo.getId();
		financeProdBuyInfo.setUserId(userId);
		if(userInfo.getRiskLevel() < 0){
			logger.error("用户未做风险评测，请做完风险评测再购买理财产品");
			throw new Exception("用户未做风险评测，请做完风险评测再购买理财产品");
		}
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
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"),
			@ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/funds/sell", method = RequestMethod.POST)
	public ResponseEntity<?> sellFinanceProd(@RequestBody ProdSellPageDTO prodSellPageDTO)
			throws Exception {
		if (prodSellPageDTO.getUserId() == 0L) {
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
	 * 赎回理财产品 页面
	 *
	 * @param prodSellPercentDTO
	 * @return
	 */
	@ApiOperation("理财赎回")
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"),
			@ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/funds/sellpersent", method = RequestMethod.POST)
	public ResponseEntity<?> sellFinanceProdPersent(@RequestBody ProdSellPercentDTO prodSellPercentDTO)
			throws Exception {
		if (prodSellPercentDTO.getUserId() == 0L) {
			logger.info("input userId is empty, need retrieve userId");
			Long userId = tradeOpService.getUserId(prodSellPercentDTO.getUserUuid());
			prodSellPercentDTO.setUserId(userId);
		}
//		Long userId = tradeOpService.getUserId(financeProdBuyInfo.getUuid());
//		financeProdBuyInfo.setUserId(userId);
		TrdOrder trdOrder = tradeSellService.sellProductPercent(prodSellPercentDTO);
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
			@ApiImplicitParam(paramType = "query", name = "totalAmount", dataType = "BigDecimal", required = true, value = "购买金额", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "Integer", required = true, value = "oemid", defaultValue = "1"),})
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	@RequestMapping(value = "/funds/buyProduct", method = RequestMethod.GET)
	public ResponseEntity<DistributionResult> buyProduct(
			@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "subGroupId") Long subGroupId,
			@RequestParam(value = "totalAmount") BigDecimal totalAmount,
			@RequestParam(value = "oemid") Integer oemid)
			throws Exception {
		ProductBaseInfo productBaseInfo = new ProductBaseInfo();
		productBaseInfo.setProdId(groupId);
		productBaseInfo.setGroupId(subGroupId);
		productBaseInfo.setOemId(oemid);
		logger.info("groupId:{} subGroupId:{} oemid:{}", groupId, subGroupId, oemid);
		List<ProductMakeUpInfo> productList = financeProdInfoService.getFinanceProdMakeUpInfo(productBaseInfo);
		//最大金额最小金额判断
		boolean result = financeProdCalcService.getMaxMinResult(productList, totalAmount);
		if (!result) {
			logger.error("输入金额数不在最大最小限制内，请重新输入");
			throw new Exception("输入金额数不在最大最小限制内，请重新输入");
		}
		DistributionResult distributionResult = financeProdCalcService.getPoundageOfBuyFund(totalAmount, productList);
		return new ResponseEntity<DistributionResult>(distributionResult, HttpStatus.OK);
	}



	@ApiOperation("获取组合产品详细信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "Long", required = true, value = "groupId", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "Long", required = true, value = "subGroupId", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "Integer", required = true, value = "oemid", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "minValue", dataType = "BigDecimal", required = true, value = "minValue"),
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	@RequestMapping(value = "/funds/getFundDetailList", method = RequestMethod.GET)
	public ResponseEntity<HashMap> getFundDetailList(
			@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "subGroupId") Long subGroupId,
			@RequestParam(value = "oemid") Integer oemid,
			@RequestParam(value = "minValue") BigDecimal minValue) throws Exception {
				java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
				HashMap<Object, Object> fundDetailMap = new HashMap<>();
				List<FundDetailResult> fundDetailList=new ArrayList<>();
				ProductBaseInfo productBaseInfo = new ProductBaseInfo();
				productBaseInfo.setProdId(groupId);
				productBaseInfo.setGroupId(subGroupId);
				productBaseInfo.setOemId(oemid);
				List<ProductMakeUpInfo> productList = financeProdInfoService.getFinanceProdMakeUpInfo(productBaseInfo);
				DistributionResult distributionResult = financeProdCalcService.getPoundageOfBuyFund(minValue, productList);
				fundDetailMap.put("buyRateMap",distributionResult.getBuyRateMap());
				for(ProductMakeUpInfo productMakeUpInfo: productList){
					BigDecimal fundShare = BigDecimal.valueOf(productMakeUpInfo.getFundShare()).divide(BigDecimal.valueOf(10000));
					String fundShareStr = df.format(fundShare.multiply(new BigDecimal("100")))+"%";
					FundDetailResult fundDetailResult = new FundDetailResult(productMakeUpInfo.getFundCode(),productMakeUpInfo.getFundName(),"--",fundShareStr);
					fundDetailList.add(fundDetailResult);
				}
				fundDetailMap.put("fundAmountList",fundDetailList);
				return new ResponseEntity<HashMap>(fundDetailMap, HttpStatus.OK);
	}

	@ApiOperation("获取默认选择银行卡信息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "bankcardNumbers", dataType = "String", required = true, value = "bankcardNumbers"),
			@ApiImplicitParam(paramType = "path", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = "")
			})
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	@RequestMapping(value = "/funds/getDefaultBankcard", method = RequestMethod.GET)
	public ResponseEntity<String> getDefaultBankcard(
			@RequestParam(value = "bankcardNumbers") String bankcardNumbers,
			@RequestParam(value = "uuid") String uuid
	) throws Exception {
		UserInfo userInfo = tradeOpService.getUserInfoByUserUUID(uuid);
		List<Long> bankcardList=new ArrayList<>();
		if(bankcardNumbers!=null){
			String[] bankcardArr = bankcardNumbers.split(",");
			for(String bankcard:bankcardArr){
				bankcardList.add(Long.parseLong(bankcard));
			}
		}
		Page<TrdOrder> trdOrderPage = orderService
				.getDefaultBankcardOrderByUserId(String.valueOf(userInfo.getId()), bankcardList,
						new PageRequest(0, 1));
		List<TrdOrder> trdOrderList = trdOrderPage.getContent();
		if(trdOrderList.size()>0){
			return new ResponseEntity<String>(trdOrderList.get(0).getBankCardNum(), HttpStatus.OK);
		}else {
			return null;
		}
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
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "Integer", required = true, value = "oemid", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "totalAmount", dataType = "BigDecimal", required = true, value = "赎回金额", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "persent", dataType = "BigDecimal", required = false, value = "赎回比例", defaultValue = "")
	}
			)
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	@RequestMapping(value = "/funds/sellProduct", method = RequestMethod.GET)
	public ResponseEntity<DistributionResult> sellProduct(
			@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "subGroupId") Long subGroupId,
			@RequestParam(value = "oemid") Integer oemid,
			@RequestParam(value = "totalAmount") BigDecimal totalAmount,
			@RequestParam(value = "persent",required = false,defaultValue = "0") BigDecimal persent,
			@RequestParam(value = "prodId") String prodId)


			throws Exception {
		ProductBaseInfo productBaseInfo = new ProductBaseInfo();
		productBaseInfo.setProdId(groupId);
		productBaseInfo.setGroupId(subGroupId);
		productBaseInfo.setOemId(oemid);
		List<ProductMakeUpInfo> productList = financeProdInfoService.getFinanceProdMakeUpInfo(productBaseInfo);
		DistributionResult distributionResult = financeProdCalcService.getPoundageOfSellFund(totalAmount, productList,persent,prodId);

		return new ResponseEntity<DistributionResult>(distributionResult, HttpStatus.OK);
	}

	/**
	 * 购买理财产品 产品详情
	 *
	 * @param orderId
	 * @return
	 */
	@ApiOperation("购买理财产品 产品详情页面(购买)")
	@ApiImplicitParams({
//				@ApiImplicitParam(paramType = "path", name = "uuid", dataType = "String", required = true, value = "用户UUID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "orderId", dataType = "String", required = true, value = "订单编号", defaultValue = "1231230001000001513657092497")
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	//@RequestMapping(value = "/funds/buyDetails/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Map> buyDetails_bak(
			// @PathVariable(value = "groupId") Long uuid,
			@PathVariable(value = "orderId") String orderId) throws Exception {
		logger.error("method buyDetails run ..");
		Map<String, Object> result = new HashMap<String, Object>();
		if (StringUtils.isEmpty(orderId)) {
			logger.error("购买详情不存在:{} ", orderId);
			throw new Exception("购买详情不存在:" + orderId);
		}
		TrdOrder trdOrder = orderService.getOrderByOrderId(orderId);
		List<TrdOrderDetail> trdOrderDetailList = new ArrayList<TrdOrderDetail>();
		if (trdOrder != null && trdOrder.getOrderId() != null) {
			result.put("prodId", trdOrder.getUserProdId());
			trdOrderDetailList = orderService.findOrderDetailByOrderId(orderId);
		} else {
			logger.error("购买详情不存在.");
			throw new Exception("购买详情不存在.");
		}
		//result.put("list", trdOrderDetailList);
		if (trdOrderDetailList == null || trdOrderDetailList.isEmpty()) {
			logger.error("购买详情信息数据不存在.");
			throw new Exception("购买详情信息数据不存在.");
		}
		Map<String, Object> trdOrderMap = new HashMap<String, Object>();
		TrdOrderStatusEnum[] trdOrderStatusEnum = TrdOrderStatusEnum.values();
		for (TrdOrderStatusEnum trdOrderStatus : trdOrderStatusEnum) {
			if (trdOrder.getOrderStatus() == trdOrderStatus.getStatus()) {
				result.put("orderStatus", trdOrderStatus.getComment());
				break;
			} else {
				result.put("orderStatus", "");
			}
		}

		TrdOrderOpTypeEnum[] trdOrderOpType = TrdOrderOpTypeEnum.values();
		for (TrdOrderOpTypeEnum trdOrderOpTypeEnum : trdOrderOpType) {
			if (trdOrder.getOrderType() == trdOrderOpTypeEnum.getOperation()) {
				result.put("orderType", trdOrderOpTypeEnum.getComment());
				break;
			} else {
				result.put("orderType", "");
			}
		}
		//TODO title
		//result.put("title", "稳健型-3个月组合");
		//金额
		long amount = trdOrder.getPayAmount();
		if (amount != 0) {
			amount = amount / 100L;
		}
		result.put("amount", amount);
		//手续费
		if (trdOrder.getPayFee() == null) {
			result.put("payfee", "");
		} else {
			result.put("payfee", trdOrder.getPayFee());
		}
		//状态详情
		List<Map<String, Object>> detailList = new ArrayList<Map<String, Object>>();
		Map<String, Object> detailMap = new HashMap<String, Object>();
		for (int i = 0; i < trdOrderDetailList.size(); i++) {
			detailMap = new HashMap<String, Object>();
			TrdOrderDetail trdOrderDetail = trdOrderDetailList.get(i);

			TrdOrderStatusEnum[] trdOrderStatusEnum2 = TrdOrderStatusEnum.values();
			for (TrdOrderStatusEnum trdOrderStatus2 : trdOrderStatusEnum2) {
				if (trdOrderDetail.getOrderDetailStatus() == trdOrderStatus2.getStatus()) {
					detailMap.put("fundstatus", trdOrderStatus2.getComment());
					break;
				} else {
					detailMap.put("fundstatus", "");
				}
			}
			detailMap.put("fundCode", trdOrderDetail.getFundCode());
			//基金费用
			detailMap.put("fundbuyFee", trdOrderDetail.getBuyFee());
			Instant instance = Instant.now();
			Long instanceLong = instance.toEpochMilli();
			String date = InstantDateUtil.getTplusNDayNWeekendOfWork(instanceLong, 1);
			detailMap.put("funddate", date);

			TrdOrderOpTypeEnum[] trdOrderOpTypeEnum = TrdOrderOpTypeEnum.values();
			for(TrdOrderOpTypeEnum trdOrder3 : trdOrderOpTypeEnum){
				if(trdOrderDetail.getTradeType() == trdOrder3.getOperation()){
					detailMap.put("fundTradeType", trdOrder3.getComment());
					break;
				} else {
					detailMap.put("fundTradeType", "");
				}
			}
			detailList.add(detailMap);
		}
		result.put("detailList", detailList);

		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}
	/**
	 * 购买理财产品 产品详情
	 *
	 * @param orderId
	 * @return
	 */
	@ApiOperation("购买理财产品 产品详情页面(购买)")
	@ApiImplicitParams({
//				@ApiImplicitParam(paramType = "path", name = "uuid", dataType = "String", required = true, value = "用户UUID", defaultValue = ""),
		@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "订单编号", defaultValue = "1231230001000001513657092497")
	})
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
		@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
		@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	@RequestMapping(value = "/funds/buyDetails/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Map> buyDetails(
			// @PathVariable(value = "groupId") Long uuid,
			@PathVariable(value = "orderId") String orderId) throws Exception {
		logger.error("method buyDetails run ..");
		Map<String, Object> result = tradeOpService.buyDeatils(orderId);
		
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}
	
	/**
	 * 赎回理财产品 产品详情
	 *
	 * @param orderId
	 * @return
	 */
	@ApiOperation("购买理财产品 产品详情页面(赎回)")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "path", name = "orderId", dataType = "String", required = true, value = "订单编号", defaultValue = "1231230001000001513657092497")
	})
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
		@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
		@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	@RequestMapping(value = "/funds/sellDetails/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Map> sellDetails(
			// @PathVariable(value = "groupId") Long uuid,
			@PathVariable String orderId) throws Exception {
		logger.error("method sellDetails run ..");
		Map<String, Object> result = tradeOpService.sellDeatils(orderId);
//		Map<String, Object> result = new HashMap<String, Object>();
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
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"),
			@ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/funds/buywithpreorder", method = RequestMethod.POST)
	public ResponseEntity<?> buyFinanceProdWithPreOrder(@RequestBody FinanceProdBuyInfo
																financeProdBuyInfo)
			throws Exception {
		Long userId = null;
		if (null == financeProdBuyInfo.getUserId()) {
			logger.info("input userId is empty, need retrieve userId");
			userId = tradeOpService.getUserId(financeProdBuyInfo.getUuid());
			financeProdBuyInfo.setUserId(userId);
		}
		if (null == userId) {
			logger.error("cannot find userId for user:" + financeProdBuyInfo.getUuid());
			return new ResponseEntity<Object>("cannot find userId for user:" + financeProdBuyInfo
					.getUuid(), HttpStatus.NOT_ACCEPTABLE);
		}
		financeProdBuyInfo.setUserId(userId);
		financeProdBuyInfo.setMoney(financeProdBuyInfo.getMoney());
		TrdOrder trdOrder = tradeOpService.buyFinanceProductWithPreOrder(financeProdBuyInfo);
		return new ResponseEntity<Object>(trdOrder, HttpStatus.OK);
	}
	
	/**
	 * 获取银行卡号码
	 *
	 * @param 
	 * @return
	 */
	@ApiOperation("获取银行卡号码")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "path", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "Long", required = true, value = "产品ID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "orderType", dataType = "Integer", required = true, value = "交易类型", defaultValue = "")
	})
	@ApiResponses({ @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") })
	@RequestMapping(value = "/funds/banknums/{uuid}", method = RequestMethod.GET)
	public ResponseEntity<Map> getOrderInfos(@PathVariable(value = "uuid") String uuid,
			@RequestParam(value = "prodId") Long prodId,
			@RequestParam(value = "orderType") int orderType) throws Exception {
//		logger.error("method getBanknums run ..");
		Map<String, Object> result = new HashMap<String, Object>();
		result = tradeOpService.getOrderInfos(uuid, prodId,orderType);
		return new ResponseEntity<Map>(result, HttpStatus.OK);
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
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "Long", required = true, value = "subGroupId", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "Integer", required = true, value = "oemid", defaultValue = "1")
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	@RequestMapping(value = "/funds/maxminValue", method = RequestMethod.GET)
	public ResponseEntity<Map> getMaxMinValue(
			@RequestParam(value = "groupId") Long groupId,
			@RequestParam(value = "subGroupId") Long subGroupId,
			@RequestParam(value = "oemid") Integer oemid)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		ProductBaseInfo productBaseInfo = new ProductBaseInfo();
		productBaseInfo.setProdId(groupId);
		productBaseInfo.setGroupId(subGroupId);
		productBaseInfo.setOemId(oemid);
		List<ProductMakeUpInfo> productList = financeProdInfoService.getFinanceProdMakeUpInfo(productBaseInfo);
		BigDecimal min = financeProdCalcService.getMinBuyAmount(productList);
		BigDecimal max = financeProdCalcService.getMaxBuyAmount(productList);
		result.put("min", min);
		result.put("max", max);
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}
	
	/**
	 * 获取中正银行信息
	 *
	 * @return
	 */
	@ApiOperation("获取中正银行信息")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "bankShortName", dataType = "String", required = true, value = "银行简称", defaultValue = "")
	})
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
		@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
		@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	@RequestMapping(value = "/funds/banks", method = RequestMethod.GET)
	public ResponseEntity<Map> getBanks(@RequestParam(value = "bankShortName") String bankShortName) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result = orderService.getBankInfos(bankShortName);
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}

	/**
	 * 同步中正银行信息
	 *
	 * @return
	 */
	@ApiOperation("获取中正银行信息")
	@ApiImplicitParams({
	})
	@ApiResponses({
			@ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
			@ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
			@ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
			@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
	@RequestMapping(value = "/funds/syncbanks", method = RequestMethod.GET)
	public ResponseEntity<Map> syncBanks()
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		orderService.syncBankInfos();
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}
	
	/**
     * 支持的银行列表
     *
     * @return
     */
    @ApiOperation("支持的银行列表")
    @ApiImplicitParams({
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 204, message = "OK"),
            @ApiResponse(code = 400, message = "请求参数没填好"), @ApiResponse(code = 401, message = "未授权用户"),
            @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")})
    @RequestMapping(value = "/funds/banklists", method = RequestMethod.GET)
    public ResponseEntity<Map> getBanklists() throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        result = orderService.getBanklists();
        return new ResponseEntity<Map>(result, HttpStatus.OK);
    }

}
