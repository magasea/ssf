package com.shellshellfish.aaas.transfer.controller;

import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.model.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("智投组合相关接口")
public class FundGroupController {

	Logger logger = LoggerFactory.getLogger(FundGroupController.class);

	@Autowired
	RestTemplate restTemplate;


	@Value("${shellshellfish.user-user-info}")
	private String userinfoUrl;
	
	@Value("${shellshellfish.trade-order-url}")
	private String tradeOrderUrl;

	@ApiOperation("我的智投组合详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = "shellshellfish"),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品ID", defaultValue = "41"),
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "12"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "120049"),})
	@RequestMapping(value = "/getMyProductDetail", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getProductDetail(@RequestParam String uuid, @RequestParam String prodId,@RequestParam String groupId,@RequestParam String subGroupId) {

		Map result = null;

		String methodUrl = "/api/userinfo/getMyProductDetail";
		Map<String, String> params = new HashMap(2);

		params.put("uuid", uuid);
		params.put("prodId", prodId);

		ResponseEntity<Map> entity = restTemplate.postForEntity(URLutils.prepareParameters(userinfoUrl + methodUrl, params), HttpEntity.EMPTY, Map.class, params);
		if (HttpStatus.OK.equals(entity.getStatusCode())) {
			result = entity.getBody();
			result.put("groupId", groupId);
			result.put("subGroupId", subGroupId);
			Map bankNumResult = restTemplate
					.getForEntity(tradeOrderUrl + "/api/trade/funds/banknums/" + uuid + "?prodId=" + prodId, Map.class)
					.getBody();
			if(bankNumResult.get("bankNum")!=null){
				String bankNum = bankNumResult.get("bankNum")+"";
				String bankName = "";
				List bankList = restTemplate.getForEntity(userinfoUrl + "/api/userinfo/users/" + uuid + "/bankcards", List.class).getBody();
				if(bankList!=null){
					for(int i=0;i<bankList.size();i++){
						Map bankMap = (Map) bankList.get(i);
						if(bankNum.equals(bankMap.get("bankcardNum"))){
							if(bankMap.get("bankShortName")!=null){
								bankName = bankMap.get("bankShortName")+"";
//								String bankcardSecurity[] = (bankMap.get("bankcardSecurity").toString()).split(" ");
//								bankNum = bankcardSecurity[bankcardSecurity.length-1];
								bankNum = bankNum.substring(bankNum.length()-4);
								break;
							}
						}
					}
					result.put("bankNum", bankNum);
					result.put("bankName", bankName);
				}
			}
					
		} else {
			logger.error("error code : {} ; error message :{}", entity.getStatusCode(), entity.getBody());
			return new JsonResult(JsonResult.Fail, "获取失败", JsonResult.EMPTYRESULT);
		}
		
		


		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}


}
