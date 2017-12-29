package com.shellshellfish.aaas.common.utils;

import java.util.Map;

/**
 *  URL 处理工具类
 */
public class URLutils {


	/**
	 * 拼接get请求工具
	 * @param url
	 * @param params
	 * @return
	 */
	public static  String prepareParameters(String url, Map<String, String> params) {
		url += "?";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			url += entry.getKey() + "=" + entry.getValue() + "&";
		}
		return url.substring(0, url.lastIndexOf("&"));

	}
	
	
}
