package com.szbsc.entity;

import org.apache.shiro.authc.AuthenticationToken;

@SuppressWarnings("serial")
public class JwtToken implements AuthenticationToken {

	private String token;
	
	public JwtToken() {
	}

	public JwtToken(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return this.token;
	}

	@Override
	public Object getCredentials() {
		return this.token;
	}
}