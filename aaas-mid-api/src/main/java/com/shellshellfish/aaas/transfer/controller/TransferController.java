package com.shellshellfish.aaas.transfer.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.shellshellfish.aaas.dto.FinanceProdBuyInfo;
import com.shellshellfish.aaas.dto.FinanceProdSellInfo;
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
	
	@Value("${shellshellfish.data-manager-url}")
	private String dataManagerUrl;
	
	
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
		  return new JsonResult(JsonResult.Fail,"手机验证失败，申购失败", JsonResult.EMPTYRESULT);
		}
		//验证码不通过则直接返回失败
		if ("验证失败".equals(verify)){
			return new JsonResult(JsonResult.Fail,"手机验证失败，申购失败", JsonResult.EMPTYRESULT);
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
			return new JsonResult(JsonResult.Fail,str , JsonResult.EMPTYRESULT);
		}
	}
	
	
	@ApiOperation("理财产品 产品详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="orderId",dataType="String",required=true,value="订单编号",defaultValue="1231230001000001513657092497")
	})
	@RequestMapping(value="/buyDetails",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult buyDetails(@RequestParam String orderId){
		Map<Object, Object> result = new HashMap<Object, Object>();
		try{
			result = restTemplate
					.getForEntity(tradeOrderUrl + "/api/trade/funds/buyDetails/" + orderId , Map.class).getBody();
			if (result == null || result.size() == 0) {
				logger.error("产品详情-result-获取失败");
				return new JsonResult(JsonResult.Fail, "产品详情获取失败", JsonResult.EMPTYRESULT);
			}
			if(result.get("detailList")==null){
				logger.error("产品详情-detailList-获取失败");
				//return new JsonResult(JsonResult.Fail, "产品详情获取失败", JsonResult.EMPTYRESULT);
			} else {
				List detail = (List) result.get("detailList");
				if(detail!=null || detail.size()!=0){
					for(int i=0;i<detail.size();i++){
						if(detail.get(i)!=null){
							Map map = (Map) detail.get(i);
							String fundCode = (String) map.get("fundCode");
							if(!StringUtils.isEmpty(fundCode)){
								List fundList = new ArrayList();
								fundList = restTemplate.getForEntity(dataManagerUrl + "/api/datamanager/getFundInfo?codes=" + fundCode , List.class).getBody();
								if(fundList==null||fundList.size()==0){
									logger.error("基金CODE:"+fundCode+"不存在");
								} else {
									String fundName = (String) ((Map)fundList.get(0)).get("name");
									map.put("fundName", fundName);
									map.remove("fundCode");
								}
							}
						}
					}
				}
//				if(detailList!=null && detailList){
//					
//				}
			}
			return new JsonResult(JsonResult.SUCCESS, "产品详情页面成功", result);
		}catch(Exception e){
			logger.error("产品详情页面接口失败");
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail,"产品详情页面失败" , JsonResult.EMPTYRESULT);
		}
	}
	
	
	
	@ApiOperation("产品赎回")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="手机号",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="verifyCode",dataType="String",required=true,value="验证码",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="userProdId",dataType="String",required=true,value="",defaultValue="46"),
		@ApiImplicitParam(paramType="query",name="prodId",dataType="String",required=true,value="产品的prodId",defaultValue="2"),
		@ApiImplicitParam(paramType="query",name="groupId",dataType="String",required=true,value="产品的groupId",defaultValue="2000"),	
		@ApiImplicitParam(paramType="query",name="userUuid",dataType="String",required=true,value="客户uuid",defaultValue="shellshellfish"),
	})
	@RequestMapping(value="/sellProduct",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult sellProduct(@RequestParam String telNum,@RequestParam String verifyCode,
			@RequestParam String userProdId,@RequestParam String prodId,@RequestParam String groupId,@RequestParam String userUuid,
			@RequestBody List<FinanceProdSellInfo> infoList){
		//首先调用手机验证码
		String verify=null;
		try{
			verify=service.verifyMSGCode(telNum, verifyCode);
			}catch(Exception e){
				String str=new ReturnedException(e).getErrorMsg();
			    logger.error(str);	
			  return new JsonResult(JsonResult.Fail,"手机验证失败，赎回失败", JsonResult.EMPTYRESULT);
		        }
		//验证码不通过则直接返回失败
			if ("验证失败".equals(verify)){
					return new JsonResult(JsonResult.Fail,"手机验证失败，赎回失败", JsonResult.EMPTYRESULT);
			}
		//调用赎回口
			Map result=null;
		  try{
			 result=service.sellFund(userProdId, prodId, groupId,userUuid,infoList);
		     }catch(Exception e){
		    	 logger.error("调用赎回接口发生错误");
			   e.printStackTrace();
			   return new JsonResult(JsonResult.Fail,"赎回失败", JsonResult.EMPTYRESULT);
		     }
		return new JsonResult(JsonResult.SUCCESS, "赎回成功",result);
	}
	
	
	@ApiOperation("赎回页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="groupId",dataType="String",required=true,value="groupID",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="subGroupId",dataType="String",required=true,value="subGroupId",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="totalAmount",dataType="String",required=true,value="总金额",defaultValue="")
	})
	@RequestMapping(value="/sellFundPage",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult sellFundPage(String groupId,String subGroupId,String totalAmount){
		Map result=null;
		try{
		result=service.sellFundPage(groupId, subGroupId, totalAmount);
		return new JsonResult(JsonResult.SUCCESS,"调用成功",result);
		}catch(Exception e){
			logger.error("赎回页面接口调用失败");
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}	
	}
	
	
	
}
