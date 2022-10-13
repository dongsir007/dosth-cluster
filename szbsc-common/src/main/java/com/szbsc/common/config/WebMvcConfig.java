package com.szbsc.common.config;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.szbsc.common.interceptor.HeaderInterceptor;

/**
 * 拦截器配置
 * @author Zhidong.Guo
 *
 */
public class WebMvcConfig implements WebMvcConfigurer {

	/** 不需要拦截地址 */
	public static final String[] excludeUrls = { "/login", "/lougout", "/refresh" };

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(this.getHeaderInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns(excludeUrls)
			.order(-10);
	}

	/**
	 * 自定义请求头拦截器
	 */
	private HandlerInterceptor getHeaderInterceptor() {
		return new HeaderInterceptor();
	}
}