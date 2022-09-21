package com.szbsc.util;

import java.util.UUID;

/**
 * Id生成器
 * @author Zhidong.Guo
 *
 */
public class IdGenerator {
	/**
	 * 构造
	 */
	public static String builder() {
		return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
	}
}