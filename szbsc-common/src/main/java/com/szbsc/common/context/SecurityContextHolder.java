package com.szbsc.common.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.szbsc.common.constant.SecurityConstants;
import com.szbsc.common.text.Convert;
import com.szbsc.common.utils.StringUtils;

/**
 * 获取当前线程中的用户Id、用户名称、Token等信息
 * 注意:必须在网关通过请求头的方法传入,同时在HeaderInterceptor拦截器中设置值.否则这里无法获取
 * @author Zhidong.Guo
 *
 */
public class SecurityContextHolder {
	
	private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL = new TransmittableThreadLocal<>();
	
	public static void set(String key, Object val) {
		Map<String, Object> map = getLocalMap();
		map.put(key, val == null ? StringUtils.EMPTY : val);
	}
	
	public static String get(String key) {
		Map<String, Object> map = getLocalMap();
		return Convert.toStr(map.getOrDefault(key, StringUtils.EMPTY));
	}

	public static Map<String, Object> getLocalMap() {
		Map<String, Object> map = THREAD_LOCAL.get();
		if (map == null) {
			map = new ConcurrentHashMap<>();
			THREAD_LOCAL.set(map);
		}
		return map;
	}
	
	public static void setLocalMap(Map<String, Object> threadLocalMap) {
		THREAD_LOCAL.set(threadLocalMap);
	}

	public static void remove() {
		THREAD_LOCAL.remove();
	}
	
	public static String getAccountId() {
		return get(SecurityConstants.DETAILS_ACCOUNT_ID);
	}

	public static void setAccountId(String accountId) {
		set(SecurityConstants.DETAILS_ACCOUNT_ID, accountId);
	}
	
	public static String getUserName() {
		return get(SecurityConstants.DETAILS_USER_NAME);
	}

	public static void setUserName(String userName) {
		set(SecurityConstants.DETAILS_USER_NAME, userName);
	}
	
	public static String getUserKey() {
		return get(SecurityConstants.DETAILS_USER_KEY);
	}

	public static void setUserKey(String userKey) {
		set(SecurityConstants.DETAILS_USER_KEY, userKey);
	}
}