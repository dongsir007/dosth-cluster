package com.szbsc.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.szbsc.entity.AuthResult;
import com.szbsc.util.JwtUtil;

@RestController
public class LoginController {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	/**
	 * 登录认证
	 * @param userName 用户名
	 * @param password 密码
	 * @return
	 */
	@GetMapping("/login")
	public AuthResult login(@RequestParam String userName, @RequestParam String password) {
		if ("admin".equals(userName) && "admin".equals(password)) {
			// 生成token
			String tokenId = JwtUtil.sign(userName);
			
			String refreshToken = UUID.randomUUID().toString();//数据放入redis
			
			this.redisTemplate.opsForHash().put(refreshToken, JwtUtil.TOKEN, tokenId); 
			this.redisTemplate.opsForHash().put(refreshToken, "username", userName); 
		        
		      //设置token的过期时间 
			this.redisTemplate.expire(refreshToken, JwtUtil.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS); 
		        
		    return new AuthResult(0, "success", tokenId, refreshToken); 
		} else {
			return new AuthResult(1001, "username or password error"); 
		}
	}
	
	/**
	 * 刷新token
	 * @param refreshToken
	 * @return
	 */
	@GetMapping("/refreshToken")
	public AuthResult refreshToken(@RequestParam String refreshToken) {
		String token = (String) this.redisTemplate.opsForHash().get(refreshToken, JwtUtil.TOKEN);
		if (StringUtils.isEmpty(token)) {
			return new AuthResult(1003, "refreshToken error");
		}
		// 生成新的Token
		String newToken = JwtUtil.sign(token);
		this.redisTemplate.opsForHash().put(refreshToken, JwtUtil.TOKEN, newToken);
		
		return new AuthResult(0, "success", newToken, refreshToken); 
	}
	
	@GetMapping("/") 
	public String index() { 
		return "auth-service: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); 
	} 
}