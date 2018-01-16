package com.shellshellfish.aaas.transfer.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 处理的一些小工具
 * 
 * @author developer4
 *
 */
public class EasyKit {

	private static final Logger log = LoggerFactory.getLogger(EasyKit.class);
	
	public static final String PERCENT = "%";

	public static String getErrorMessage(String jsonArray) {
		if (jsonArray == null || jsonArray.length() == 0) {
			return "";
		}
		String result = "";
		JSONObject object = JSONArray.parseObject(jsonArray);
		try {
			result = object.get("message").toString();
		} catch (Exception e) {
			log.error("解析错误码发生错误：获取不到message");
		}
		return result;
	}

	public static Map<String, Object> getMaxMinValue(List param) {
		Map<String, Object> result = new HashMap<String, Object>(); // 结果集
		if (param != null && param.size() > 0) {
			// 对数据进行解刨
			List valueList = parseValueFromValue$TimeMap(param);
			result.put("maxValue", Collections.max(valueList));
			result.put("minValue", Collections.min(valueList));
		} else {
			throw new RuntimeException("List为空值，无法解析time-value组合");
		}
		return result;
	}

	public static List parseValueFromValue$TimeMap(List param) {
		List resultList = new ArrayList<>();
		for (Object obj : param) {
			Map timeValue = (Map) obj;
			Double value = Double.parseDouble(timeValue.get("value").toString());
			resultList.add(value);
		}
		return resultList;
	}

	public static Double getDecimal(BigDecimal bigDecimal) {
		Double decimal = null;
		if(bigDecimal!=null){
			bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
			decimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return decimal;
	}
	
	public static Double getDecimalNum(BigDecimal bigDecimal) {
		Double decimal = null;
		if(bigDecimal!=null){
			//bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
			decimal = bigDecimal.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return decimal;
	}
	
	public static String getStringValue(BigDecimal bigDecimal) {
		Double decimal = null;
		StringBuffer result = new StringBuffer();
		if(bigDecimal!=null){
			bigDecimal = bigDecimal.multiply(new BigDecimal("100"));
			decimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			result.append(decimal);
			result.append("%");
		}
		if(StringUtils.isEmpty(result)){
			return "0.0%";
		} else {
			return result.toString();
		}
	}
	
//	public static void main(String[] args) {
//		System.out.println(getStringValue(new BigDecimal("0.00001049437537826574")));
//	}
}
