package com.shellshellfish.aaas.transfer.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.dto.FundNAVInfo;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.service.MidApiService;
import com.shellshellfish.aaas.transfer.controller.UserInfoController;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;
import com.shellshellfish.aaas.transfer.utils.CalculatorFunctions;

public class MidApiServiceImpl implements MidApiService {
	Logger logger = LoggerFactory.getLogger(MidApiServiceImpl.class);
@Autowired
private RestTemplate restTemplate;	
@Value("${shellshellfish.asset-alloction-url}")
private String assetAlloctionUrl;

@Value("${shellshellfish.user-login-url}")
private String loginUrl;



//获取产品详情的所有数据
@Override
public Map<String, Object> getPrdNPVList(String groupId, String subGroupId) throws Exception {
	Map<String,Object> resultMap=new HashMap<String,Object>();
	List<FundNAVInfo> resultList=new ArrayList<FundNAVInfo>();
    //获取所有产品净值增长值的list
 	List<FundNAVInfo> listA=getNPVIncrement(groupId, subGroupId);
	//获取所有产品净值增长率的list
	List<FundNAVInfo> listB=getNPVIncrementRate(groupId, subGroupId);
	//遍历每一个对象进行对比
	if(listA==null||listB==null){
		logger.error("获取净值增长值活净值增长率为null");
		throw new Exception("获取净值增长值或净值增长率为空值");
	}
	for (FundNAVInfo infoA:listA){
		for (FundNAVInfo infoB:listB){
			//对象进行比较
			FundNAVInfo info=FundNAVInfo.mergeIntoOne(infoA, infoB);
			if(info!=null){
			resultList.add(info);
			}
		}
	}
	resultMap.put("list", resultList);
	resultMap.put("total", resultList.size());	
	return resultMap;
	}
	



/**
 * 获取全部产品的NPV增值
 * @param groupId
 * @param subGroupId
 * @return
 */
	private List<FundNAVInfo> getNPVIncrement(String groupId, String subGroupId) {
		FundNAVInfo info=new FundNAVInfo();
		Map result=new HashMap<>();
		List<FundNAVInfo> resultList=new ArrayList<FundNAVInfo>();
		try{ 
			//调用组合各种类型净值收益，参数为1，获取净值走势
				String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/fund-navadj?returnType=1&id="+groupId+"&subGroupId="+subGroupId;
				result=restTemplate.getForEntity(url, Map.class).getBody();
		}catch(Exception e){
			logger.error("调用restTemplate查询净值增长数据获取失败",e.getMessage());
			return null;
		}
		//判断非空
		if(result.size()==0) {
			logger.error("查询净值增长数据为空值","数据获取失败");
			return null;
		}
        //判断结果是否有数据
		if((int)result.get("_total")==0){
			logger.error("查询净值增长数据结果为0","数据获取失败");
			return null;
		}
		//转成list
		List prdList=null;
		try{
		prdList=(List)result.get("_items");
		}catch(Exception e){
			logger.error("解析转换_items为List失败",e.getMessage());
			return null;
		}
		for(Object prd :prdList){
			//创建对象
			Map mapItem=null;
			if(!(prd instanceof Map)){
			logger.error("_item List转换为Map失败","数据获取失败");
				return null;
			}
			mapItem=(Map)prd;	
			FundNAVInfo infoA=mapToFundNAVInfo(mapItem,"1");//增长值
			resultList.add(infoA);
		}
		return resultList;
	}

	
	/**
	 * 获取全部产品的NPV增长率
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
		private List<FundNAVInfo> getNPVIncrementRate(String groupId, String subGroupId) {
			FundNAVInfo info=new FundNAVInfo();
			Map result=new HashMap<>();
			List<FundNAVInfo> resultList=new ArrayList<FundNAVInfo>();
			try{ 
				//调用组合各种类型净值收益，参数为1，获取净值走势
					String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/fund-navadj?returnType=2&id="+groupId+"&subGroupId="+subGroupId;
					result=restTemplate.getForEntity(url, Map.class).getBody();
			}catch(Exception e){
				logger.error("调用restTemplate查询净值增长数据获取失败",e.getMessage());
				return null;
			}
			//判断非空
			if(result.size()==0) {
				logger.error("查询净值增长数据为空值","数据获取失败");
				return null;
			}
	        //判断结果是否有数据
			if((int)result.get("_total")==0){
				logger.error("查询净值增长数据结果为0","数据获取失败");
				return null;
			}
			//转成list
			List prdList=null;
			try{
			prdList=(List)result.get("_items");
			}catch(Exception e){
				logger.error("解析转换_items为List失败",e.getMessage());
				return null;
			}
			for(Object prd :prdList){
				//创建对象
				Map mapItem=null;
				if(!(prd instanceof Map)){
				logger.error("_item List转换为Map失败","数据获取失败");
					return null;
				}
				mapItem=(Map)prd;	
				FundNAVInfo infoA=mapToFundNAVInfo(mapItem,"2");
				resultList.add(infoA);
			}
			return resultList;
		}
	
	
	
	
	

	@Override
	public Map<String, Object> getExpAnnualAndMaxReturn(String groupId, String subGroupId) {
		Map resultMap=new HashMap<>();
		String expAnnRate=null;
		String expMaxReturn=null;
		try{
		String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/opt?returntype=1";
		String str="{\"returntype\":\""+"1"+"\"}";
		Map result=(Map) restTemplate.postForEntity(url,getHttpEntity(str),Map.class).getBody();	
		expAnnRate=result.get("value").toString();
		resultMap.put("expAnnRate", expAnnRate);
		}catch(Exception e){
			resultMap.put("expAnnRate","");
		}
		try{
			String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/opt?returntype=2";
			String str="{\"returntype\":\""+"2"+"\"}";
			Map result=(Map) restTemplate.postForEntity(url,getHttpEntity(str),Map.class).getBody();	
			expMaxReturn=result.get("value").toString();
			resultMap.put("expMaxReturn", expMaxReturn);
			}catch(Exception e){
			resultMap.put("expMaxReturn","");
			}
		return resultMap;	
	}
	
	
	
	/**
	 * 通用方法处理post请求带requestbody
	 * 
	 * @param JsonString
	 * @return
	 */
	private HttpEntity<String> getHttpEntity(String JsonString) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json;UTF-8"));
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		HttpEntity<String> strEntity = new HttpEntity<String>(JsonString, headers);
		return strEntity;
	}

	/**
	 * 过滤取出需要的key
	 * @param map
	 * @return
	 */
	private String getMapKey(Map map){
		if(map==null){
			return null;
		}
		String[] filter= {"navadj","基金类型","name","fund_code"}; //需要过滤的key
		Set set=map.entrySet();	
		Iterator<String> it= set.iterator();
		
		while(it.hasNext()){
			String item=it.next();
			boolean flag=false;
			for(int i=0;i<filter.length;i++){
				if(filter[i].equals(item)){
				 flag=true;
				 break;
				}
			}
			if(flag=false){
			return item; 
			}
		}
		return null;
	}
	
	/**
	 * 将Map丢进去，转换成FundNAVInfo实体
	 * @param map
	 * @return
	 */
	private FundNAVInfo mapToFundNAVInfo(Map map,String flag){
		String fundCode=null;
		String name=null;
		String fundType=null;
		String avgIncreRate=null;
		if(map==null){
			logger.info("mapToFundNAVInfo:map集合为空");
			return null;
		}
		FundNAVInfo info=new FundNAVInfo();
		try{
		 fundCode=map.get("fund_code").toString(); //获取产品代码code
		 name=map.get("name").toString();//产品名称
		 fundType=map.get("基金类型").toString();//基金类型
		/************************************************/
		avgIncreRate=map.get(fundType).toString();
		/************************************************/
		}catch(Exception e){
			logger.error("获取map中的参数出错");
		}
		List NPVIncrement=null;
		try{
		 NPVIncrement=(List)map.get("navadj");//净值增长值
		}catch(Exception e){
			logger.error("净值增长值或净值增长率转换为List时出错");
		}
		info.setFundCode(fundCode);
		info.setName(name);
		info.setFundType(fundType);
		info.setAvgIncreRate(avgIncreRate);
		if("1".equals(flag)){
		info.setNPVIncrement(NPVIncrement);
		}
		if("2".equals(flag)){
		info.setNPVIncreRate(NPVIncrement);
			}
		return info;
	}

	
	
	
	@Override
	public Map<String, Object> getOptAdjustment(String riskLevel, String invstTerm) throws Exception {
		String[] field={"模拟历史年化业绩","模拟历史年化波动率","置信区间","最大亏损额","夏普比率"};//记录要获取的数据的属性值
		String[] valueInEnglish={"hisAnnualPerformanceSimu","histAnnualVolaSimu","confInterval","maxDeficit","sharpeRatio"}; //记录对应属性值的英文字段
		Map relationMap=new HashMap<>();//关系表
		
//建立对应关系		
		for (int i=0;i<field.length;i++){
			for(int j=0;j<valueInEnglish.length;j++){
				if(i==j){
				  relationMap.put(field[i], valueInEnglish[j]);
				}
			}
		}
		Map<String,Object> resultMap=new HashMap<String,Object>();
		Map container=null;
		try{
			String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/";
			MultiValueMap map=new LinkedMultiValueMap<>();
			map.add("riskLevel", riskLevel);
			map.add("investmentPeriod", invstTerm);
			container=restTemplate.postForEntity(url,map,Map.class).getBody();
		    try{
		    	List list=(List)container.get("_items");
		    	for(Object item: list){
		    		Map itemMap=(Map)item;
		    		String name=itemMap.get("name").toString();
		    		String value=itemMap.get("value").toString();
		    		//存入数据表
		    		resultMap.put(relationMap.get(name).toString(), value);
		    	}
		    }catch(Exception e){
		       throw new Exception("获取调整方案Map的Field值失败，可能Map为空");
		    }
		    String hisAnnualPerformanceSimuresult= resultMap.get("hisAnnualPerformanceSimu").toString();
	    	//计算模拟历史收益
	       resultMap.put("historicReturn",CalculatorFunctions.getHistoricReturn("10000", hisAnnualPerformanceSimuresult));
			return resultMap;
		}catch(Exception e){
			throw e;
		}	
	}




	@Override
	public String verifyMSGCode(String telNum,String msgCode) throws Exception {
			String url=loginUrl+"/api/sms/checkSmsCode?telnum="+telNum+"&verificationCode="+msgCode;
			String result=restTemplate.getForEntity(url, String.class).getBody();
			return "-1".equals(result)?"验证成功":"验证失败";
	}



	
}
