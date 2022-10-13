package com.szbsc.common;

/**
 * 响应编码
 * @author Zhidong.Guo
 *
 */
public enum ResponseCodeEnum {
	SUCCESS(0, "成功"),
	FAIL(-1, "失败"),
	LOGIN_ERROR(1000, "用户名或密码错误"),
	LOGIN_EMPTY_ERROR(1001, "用户名或密码不能为空"),
	
	UNKNOWN_ERROR(2000, "未知错误"),
	PARAMETER_ILLEGAL(2001, "参数不合法"),
	
	TOKEN_INVALID(2002, "token已过期或验证不正确"),
	TOKEN_SIGNATURE_INVALID(2003, "无效的签名"),
	TOKEN_EXPIRED(2004, "token已过期"),
	TOKEN_MISSION(2005, "token已缺失"),
	TOKEN_CHECK_INFO_FAILED(2006, "token信息验证失败"),
	REFRESH_TOKEN_INVALID(2007, "refreshtoken无效"),
	LOGOUT_ERROR(2008, "用户登出失败");
	private final int code;
	private final String message;

	private ResponseCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return this.code;
	}

	public String getMessage() {
		return this.message;
	}
}