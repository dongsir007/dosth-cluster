package com.szbsc.common;

import java.util.Objects;

import lombok.Data;

/**
 * 响应结果
 * 
 * @author Zhidong.Guo
 *
 * @param <T>
 */
@Data
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
		return buildResponseCodeEnum(ResponseCodeEnum.SUCCESS);
	}

	public static <T> ResponseResult<T> buildResponseCodeEnum(ResponseCodeEnum responseCode) {
		return new ResponseResult<>(responseCode.getCode(), responseCode.getMessage());
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
}