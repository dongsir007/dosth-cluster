package com.szbsc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * 跨域配置
 * @author Zhidong.Guo
 *
 */
@Configuration
public class CrosConfiguration {

	@Bean
	public CorsWebFilter corsWebFilter() {
		CorsConfiguration crosConfiguration = new CorsConfiguration();
		
		// 配置跨域
		// 允许所有请求头跨域
		crosConfiguration.addAllowedHeader("*");
		// 允许所有请求方法跨域
		crosConfiguration.addAllowedMethod("*");
		// 允许所有请求来源跨域
		crosConfiguration.addAllowedOrigin("*");
		// 允许携带cookie跨域,否则跨域请求会丢失cookie信息
		crosConfiguration.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", crosConfiguration);
		return new CorsWebFilter(source);
	}
}