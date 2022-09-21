package com.feign.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class FeignUser implements Serializable {
	// Id
	private String userId;
	// 姓名
	private String userName;
	// 头像
	private String avatar;

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}