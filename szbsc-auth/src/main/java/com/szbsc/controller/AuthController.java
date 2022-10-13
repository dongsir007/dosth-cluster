package com.szbsc.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.szbsc.common.ResponseCodeEnum;
import com.szbsc.common.ResponseResult;
import com.szbsc.config.JwtProperties;
import com.szbsc.entity.Account;
import com.szbsc.service.AccountService;
import com.szbsc.util.JwtUtil;

import reactor.core.publisher.Mono;

/**
 * 获取 JWT 令牌和刷新 JWT 令牌接口
 * @author Zhidong.Guo
 *
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
	@Resource
	private JwtProperties jwtProperties;
	@Autowired
	private AccountService accountService;
	@Resource
	private JwtUtil jwtUtil;
	@Resource
	private PasswordEncoder passwordEncoder;
	
	/**
	 * 用户名和密码更换JWT令牌
	 */
	@PostMapping("/login")
	public ResponseResult<?> login(@RequestBody Map<String, Object> map) {
		// 从请求中获取用户名和密码
		String accountId = (String) map.get(this.jwtProperties.getUserParamName());
		String password = (String) map.get(this.jwtProperties.getPwdParamName());
		// 用户名或密码为空
		if (StringUtils.isEmpty(accountId) || StringUtils.isEmpty(password)) {
			return ResponseResult.buildResponseCodeEnum(ResponseCodeEnum.LOGIN_EMPTY_ERROR);
		}
		// 业务层查询用户
		Account account = this.accountService.findByAccountId(accountId);
		if (account != null) {
			boolean isAuthenticated = this.passwordEncoder.matches(password, account.getPassword());
			if (isAuthenticated) {
				Map<String, Object> tokenMap = this.jwtUtil.generateTokenAndRefreshToken(accountId, account.getUserName());
				return ResponseResult.success(tokenMap);
			}
			// 密码失败
			return ResponseResult.buildResponseCodeEnum(ResponseCodeEnum.LOGIN_ERROR);
		}
		// 未找到用户
		return ResponseResult.buildResponseCodeEnum(ResponseCodeEnum.LOGIN_ERROR);
	}
	
	/**
	 * 刷新JWT令牌,用旧的令牌换到新的令牌
	 */
	@GetMapping("/refreshtoken")
	public Mono<ResponseResult<Object>> refreshToken(@RequestHeader("${szbsc.jwt.header}") String token) {
//		token = SecurityUtils.replaceTokenPrefix(token);
		if (StringUtils.isEmpty(token)) {
			return this.buildErrorResponse(ResponseCodeEnum.TOKEN_MISSION);
		}
		// 通过token获取签名,验证token是否过期
		boolean isJwtNotValid = this.jwtUtil.isTokenExpired(token);
		if (isJwtNotValid) {
			return this.buildErrorResponse(ResponseCodeEnum.TOKEN_INVALID);
		}
		// 验证token里的accountId是否为空
		String accountId = this.jwtUtil.getAccountIdFromToken(token);
		if (StringUtils.isEmpty(accountId)) {
			return this.buildErrorResponse(ResponseCodeEnum.TOKEN_CHECK_INFO_FAILED);
		}
		// 这里为了保证refreshToken只能用一次,刷新后会从redis删除
		// 用的不是redis中的refreshToken进行令牌刷新,则不能刷新
		// 使用redis中已过期的refreshToken也不能刷新令牌
		boolean isRefreshTokenNotExisted = this.jwtUtil.isRefreshTokenNotExistCache(token);
		if (isRefreshTokenNotExisted) {
			return this.buildErrorResponse(ResponseCodeEnum.REFRESH_TOKEN_INVALID);
		}
		
		String userName = this.jwtUtil.getUserNameFromToken(token);
		Map<String, Object> tokenMap = this.jwtUtil.refreshTokenAndGenerateToken(accountId, userName);
		
		return this.buildSuccessResponse(tokenMap);
	}
	
	/**
	 * 登出,删除redis中accessToken和refreshToken
	 * 只保证refreshToken不能使用,accessToken还是能使用的
	 * 如果用户拿到了之前的accessToken,则可以一直使用到过期,因为refreshToken已经无法使用了,所以保证了accessToken的时效性
	 * 下次登录时,需要重新获取新的accessToken和refreshToken,这样才能利用refreshToken进行续期
	 */
	@PostMapping("/logout")
	public Mono<ResponseResult<Object>> logout(@RequestParam("accountId") String accountId) {
		boolean logoutResult = this.jwtUtil.removeToken(accountId);
		if (logoutResult) {
			return this.buildSuccessResponse(ResponseCodeEnum.SUCCESS);
		}
		return this.buildErrorResponse(ResponseCodeEnum.LOGOUT_ERROR);
	}

	private Mono<ResponseResult<Object>> buildErrorResponse(ResponseCodeEnum responseCodeEnum) {
		return Mono.create(callback -> callback
				.success(ResponseResult.error(responseCodeEnum.getCode(), responseCodeEnum.getMessage())));
	}
	
	private Mono<ResponseResult<Object>> buildSuccessResponse(Object data) {
		return Mono.create(callback -> callback.success(ResponseResult.success(data)));
	}
}