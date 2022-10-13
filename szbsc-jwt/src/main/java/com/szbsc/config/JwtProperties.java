package com.szbsc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "passjava.jwt")
public class JwtProperties {
	private Boolean enabled; // : true # 是否开启JWT登录认证
    private String secret; // : passjava # JWT私钥,用于校验JWT令牌的合法性
    private int expiration; // : 3600000 # 令牌的有效期, 默认一小时
    private String header; // : Authorization # HTTP请求Header名称,该Header作为参数传递JWT令牌
    private String userParamName = "accountId"; // : accountId # 用户登录认证用户参数名称
    private String pwdParamName = "password"; // : password # 用户登录认证密码参数名称
    private Boolean useDefaultController = false; // : true # 是否使用默认的JwtAuthController
    private String skipValidUrl; // : /auth/login,/renren-fast/captcha.jpg,/renren-fast/sys/login 
}