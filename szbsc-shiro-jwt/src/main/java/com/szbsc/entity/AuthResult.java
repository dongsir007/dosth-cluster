package com.szbsc.entity;

public class AuthResult {
	private int code;
	private String messge;
	private String token;
	private String refreshToken;

	public AuthResult() {
	}

	public AuthResult(int code, String messge) {
		this.code = code;
		this.messge = messge;
	}

	public AuthResult(int code, String messge, String token, String refreshToken) {
		this.code = code;
		this.messge = messge;
		this.token = token;
		this.refreshToken = refreshToken;
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessge() {
		return this.messge;
	}

	public void setMessge(String messge) {
		this.messge = messge;
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return this.refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}