package com.shellshellfish.aaas.transfer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.model.JsonResult;
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
	   return new JsonResult(JsonResult.SUCCESS,"获取成功",resultMap);
	  }catch(Exception e){
		  String str=new ReturnedException(e).getErrorMsg();
		  return new JsonResult(JsonResult.Fail,str, "");
	  }
	}
	
	
	@ApiOperation("申购基金")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="",defaultValue="")
	})
	@RequestMapping(value="/subscribeFund",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult doTransaction(){
		//首先验证验证码
		
		
		
		
		return new JsonResult(JsonResult.SUCCESS, "请求成功", null);
	}
	
}
