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
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
}