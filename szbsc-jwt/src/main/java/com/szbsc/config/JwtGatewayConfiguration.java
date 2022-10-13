package com.szbsc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Gateway配置
 * @author Zhidong.Guo
 *
 */
@Configuration
public class JwtGatewayConfiguration {
	
	/**
	 * 用户注册的密码也是经过BCryptPasswordEncoder.encode加密后存放到数据库
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}