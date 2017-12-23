package com.shellshellfish.aaas.transfer.utils;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 处理的一些小工具
 * @author developer4
 *
 */
public class EasyKit {
	
	
	private static final Logger log = LoggerFactory.getLogger(EasyKit.class);

	

	public static String getErrorMessage(String jsonArray){
		if(jsonArray==null||jsonArray.length()==0){
			return "";
		}
	    String result="";
		JSONObject object=JSONArray.parseObject(jsonArray);
		try{
		 result=object.get("message").toString();
		}catch(Exception e){
		 log.error("解析错误码发生错误：获取不到message");
		}
		return result;
	}
	
	
	
}
