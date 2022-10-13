package com.szbsc.common;

import java.util.Objects;

/**
 * 响应结果
 * 
 * @author Zhidong.Guo
 *
 * @param <T>
 */
public class ResponseResult<T> {
	private int code = 0;
	private String message;
	private T data;

	public ResponseResult(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public ResponseResult(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static <T> ResponseResult<T> success() {
		return new ResponseResult<>(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage());
	}

	public static <T> ResponseResult<T> success(T data) {
		return new ResponseResult<>(ResponseCodeEnum.SUCCESS.getCode(), ResponseCodeEnum.SUCCESS.getMessage(), data);
	}

	public static <T> ResponseResult<T> error(int code, String msg) {
		return new ResponseResult<>(code, msg);
	}

	public static <T> ResponseResult<T> error(int code, String msg, T data) {
		return new ResponseResult<>(code, msg, data);
	}

	public boolean isSuccess() {
		return Objects.equals(code, ResponseCodeEnum.SUCCESS.getCode());
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data) {
		this.data = data;
	}
}