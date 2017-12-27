package com.shellshellfish.aaas.transfer.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.dto.FinanceProdBuyInfo;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.service.MidApiService;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 交易用
 * @author developer4
 *
 */
@RestController
@RequestMapping("/phoneapi-ssf")
@Api("转换相关restapi")
public class TransferController {
	
   Logger logger =LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MidApiService service;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${shellshellfish.trade-order-url}")
	private String tradeOrderUrl;
	
	
	@ApiOperation("获取预计费用,以及投资组合的每一支基金的费用")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="groupId",dataType="String",required=true,value="groupId",defaultValue="2"),
		@ApiImplicitParam(paramType="query",name="subGroupId",dataType="String",required=true,value="subGroupId",defaultValue="2000"),
		@ApiImplicitParam(paramType="query",name="totalAmount",dataType="String",required=true,value="购买的总金额",defaultValue="")
	})
	@RequestMapping(value="/getEstPurAmount",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult getEstPurAmount(String groupId,String subGroupId,String totalAmount){
	  Map resultMap=null;
	  try{
	   String url=tradeOrderUrl+"/api/trade/funds/buyProduct?groupId="+groupId+"&subGroupId="+subGroupId+"&totalAmount="+totalAmount; 
	   resultMap= restTemplate.getForEntity(url,Map.class).getBody();
	   BigDecimal poundage=BigDecimal.valueOf(Double.parseDouble(resultMap.get("poundage").toString()));
	   BigDecimal discount=BigDecimal.valueOf(Double.parseDouble( resultMap.get("discountSaving").toString()));
	   BigDecimal total=poundage.add(BigDecimal.valueOf(Double.parseDouble((totalAmount))));
	   BigDecimal totalOffDiscount=total.add(discount);
	   resultMap.put("total", total);
	   resultMap.put("originalCost", totalOffDiscount);
	   return new JsonResult(JsonResult.SUCCESS,"获取成功",resultMap);
	  }catch(Exception e){
		  String str=new ReturnedException(e).getErrorMsg();
		  return new JsonResult(JsonResult.Fail,str, JsonResult.EMPTYRESULT);
	  }
	}
	
	
	@ApiOperation("申购基金")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="电话号码",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="msgCode",dataType="String",required=true,value="验证码",defaultValue="")
	})
	@RequestMapping(value="/subscribeFund",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult doTransaction(@RequestParam String telNum,@RequestParam String msgCode,@RequestBody FinanceProdBuyInfo prdInfo){
		String verify=null;
		//首先验证验证码\
		try{
		verify=service.verifyMSGCode(telNum, msgCode);
		}catch(Exception e){
			String str=new ReturnedException(e).getErrorMsg();
		    logger.error(str);	
		  return new JsonResult(JsonResult.Fail,"手机验证失败", JsonResult.EMPTYRESULT);
		}
		//验证码不通过则直接返回失败
		if ("验证失败".equals(verify)){
			return new JsonResult(JsonResult.Fail,"手机验证失败", JsonResult.EMPTYRESULT);
		}
		try{
		//调用购买接口
		Map buyProductSuccess=service.buyProduct(prdInfo);
		Map resultMap=new HashMap<>();
		resultMap.put("orderId", buyProductSuccess.get("orderId").toString());
		return new JsonResult(JsonResult.SUCCESS, "购买成功", resultMap);
		}catch(Exception e){
			logger.error("购买基金调用购买接口失败");
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail,"购买失败" , JsonResult.EMPTYRESULT);
		}
	}
	
}
