/**
 * 
 */
package com.shellshellfish.aaas.account.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest; 
import java.security.NoSuchAlgorithmException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenyuan
 * @company shellshellfish
 */
public class MD5 {
	
	public static final Logger logger = LoggerFactory.getLogger(MD5.class);
	
	private static Log log = LogFactory.getLog(MD5.class);
	
	/**
	 * Used building output as Hex
	 */
	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * 
	* @Title: getMD5
	* @Description: 将字符串转换成MD5码字符串
	* @param s 字符串
	* @return String MD5码
	 */
	public static String getMD5(String s){ 
		MessageDigest msgDigest = null;

		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error("System doesn't support MD5 algorithm.", e);
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}

		try {
			msgDigest.update(s.getBytes("utf-8"));    //注意改接口是按照utf-8编码形式加密
 
		} catch (UnsupportedEncodingException e) {
			logger.error("System doesn't support your  EncodingException.", e);
			throw new IllegalStateException(
					"System doesn't support your  EncodingException.");

		}

		byte[] bytes = msgDigest.digest();

		String md5Str = new String(encodeHex(bytes));

		return md5Str;
	}
	
	public static char[] encodeHex(byte[] data) {

		int l = data.length;

		char[] out = new char[l << 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
			out[j++] = DIGITS[0x0F & data[i]];
		}

		return out;
	}

}
