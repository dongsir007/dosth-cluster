package com.szbsc.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import com.szbsc.common.constant.Constants;

/**
 * 客户端工具类
 * @author Zhidong.Guo
 *
 */
public class ServletUtils {

	/**
	 * 获取请求头参数
	 * @param request 请求
	 * @param name 参数名称
	 * @return
	 */
	public static String getHeader(HttpServletRequest request, String name) {
		String value = request.getHeader(name);
		if (StringUtils.isEmpty(value)) {
			return StringUtils.EMPTY;
		}
		return null;
	}
	
	/**
	 * 内容解码
	 * @param str 内容
	 * @return 解码后的内容
	 */
	public static String urlDecode(String str) {
		try {
			return URLDecoder.decode(str, Constants.UTF8);
		} catch (UnsupportedEncodingException e) {
			return StringUtils.EMPTY;
		}
	}
}