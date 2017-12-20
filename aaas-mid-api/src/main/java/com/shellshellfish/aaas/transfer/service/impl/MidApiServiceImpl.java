package com.shellshellfish.aaas.transfer.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.service.MidApiService;

public class MidApiServiceImpl implements MidApiService {

@Autowired
private RestTemplate restTemplate;	
@Value("${shellshellfish.asset-alloction-url}")
private String assetAlloctionUrl;
	
	
	@Override
	public Map<String, Object> getPrdNPVIncrement(String groupId, String subGroupId) {
		Map result=new HashMap<>();
		try{
			//调用组合各种类型净值收益，参数为1，获取净值走势
				String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/fund-navadj?returnType="+"1";
				result=restTemplate.getForEntity(url, Map.class,groupId,subGroupId).getBody();
		}catch(Exception e){
			result.put("数据获取失败", "调用restTemplate查询净值增长数据获取失败"+e.getMessage());
			return result;
		}
		//判断非空
		if(result.size()==0) {
			result.put("数据获取失败","查询净值增长数据为空值");
			return result;
		}
        //判断结果是否有数据
		if((int)result.get("_total")==0){
			result.put("数据获取失败","查询净值增长数据结果为0");
			return result;
		}
		//转成list
		List prdList=(List)result.get("_items");
		for(Object prd :prdList)
		
		
		
		
		
		
		return result;
		
		
		
	}

}
