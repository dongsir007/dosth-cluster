package com.szbsc.config;

import org.springframework.core.Ordered;

public class JwtFilter implements Ordered {

	@Override
	public int getOrder() {
		return -100;
	}
}