package com.szbsc.filter;

import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSON;
import com.szbsc.common.ResponseCodeEnum;
import com.szbsc.common.ResponseResult;
import com.szbsc.common.constant.TokenConstants;
import com.szbsc.common.utils.ServletUtils;
import com.szbsc.common.utils.StringUtils;
import com.szbsc.config.JwtProperties;
import com.szbsc.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class JwtAuthCheckFilter {
	private static final String AUTH_TOKEN_URL = "/auth/login";
//	private static final String RENREN_CAPTCHA_TOKEN_URL = "/renren-fast/captcha.jpg";
//	private static final String RENREN_LOGIN_TOKEN_URL = "/renren-fast/sys/login";
//	private static final String RENREN_START_URL = "/renren-fast";
	public static final String ACCOUNT_ID = "accountId";
	public static final String USER_NAME = "userName";
	public static final String FROM_SOURCE = "from-source";
	
	@Resource
	private JwtProperties jwtProperties;
	@Resource
	private JwtUtil jwtUtil;
	
	@Bean
	@Order(-101)
	public GlobalFilter jwtAuthGlobalFilter() {
		return (exchange, chain) -> {
			// renren-fast自带token认证,gateway不需要做登录认证了,跳过token验证,转发所有请求
			boolean flag = false;
			if (flag) {
				return chain.filter(exchange);
			}
			ServerHttpRequest serverHttpRequest = exchange.getRequest();
			ServerHttpResponse serverHttpResponse = exchange.getResponse();
			String requestUrl = serverHttpRequest.getURI().getPath();
			// 跳过对登录请求的token检查,因为登录请求是没有token的
			if (AUTH_TOKEN_URL.equals(requestUrl)) {
				return chain.filter(exchange);
			}
			// 从http请求头中获取JWT令牌
			String token = this.getToken(serverHttpRequest);
			if (StringUtils.isEmpty(token)) {
				return this.unauthorizedResponse(exchange, serverHttpResponse, ResponseCodeEnum.TOKEN_MISSION);
			}
			// 对token解签名并验证token是否过期
			boolean isJwtNotValid = this.jwtUtil.isTokenExpired(token);
			if (isJwtNotValid) {
				return this.unauthorizedResponse(exchange, serverHttpResponse, ResponseCodeEnum.TOKEN_INVALID);
			}
			// 验证token里的accountId是否为空
			String accountId = this.jwtUtil.getAccountIdFromToken(token);
			if (StringUtils.isEmpty(accountId)) {
				return this.unauthorizedResponse(exchange, serverHttpResponse, ResponseCodeEnum.TOKEN_CHECK_INFO_FAILED);
			}
			// 验证token里面accountId是否为空
			ServerHttpRequest.Builder mutate = serverHttpRequest.mutate();
			String userName = this.jwtUtil.getUserNameFromToken(token);
			this.addHeader(mutate, ACCOUNT_ID, accountId);
			this.addHeader(mutate, USER_NAME, userName);
			// 内部请求来源参数清除
			this.removeHeader(mutate, FROM_SOURCE);
			return chain.filter(exchange.mutate().request(mutate.build()).build());
		};
	}
	
	private void addHeader(ServerHttpRequest.Builder mutate, String name, Object val) {
		if (val == null) {
			return;
		}
		mutate.header(name, ServletUtils.urlDecode(val.toString()));
	}
	
	private void removeHeader(ServerHttpRequest.Builder mutate, String name) {
		mutate.headers(httpHeaders -> httpHeaders.remove(name)).build();
	}
	
	/**
	 * 获取请求token
	 */
	private String getToken(ServerHttpRequest request) {
		String token = request.getHeaders().getFirst(this.jwtProperties.getHeader());
		// 如果前端设置了令牌前缀,则裁剪掉前缀
		if (StringUtils.isNotEmpty(token) && token.startsWith(TokenConstants.PREFIX)) {
			token = token.replaceFirst(TokenConstants.PREFIX, StringUtils.EMPTY);
		}
		return token;
	}
	
	/**
	 * 将jwt鉴权失败的消息响应给客户端
	 */
	private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, ServerHttpResponse serverHttpResponse, ResponseCodeEnum responseCodeEnum) {
		log.error("鉴权异常处理,请求路径:{}", exchange.getRequest().getPath());
		serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
		serverHttpResponse.getHeaders().add("Content-Type", "application/json;charset=UTF-8");
		ResponseResult<Object> responseResult = ResponseResult.buildResponseCodeEnum(responseCodeEnum);
		DataBuffer dataBuffer = serverHttpResponse.bufferFactory()
				.wrap(JSON.toJSONStringWithDateFormat(responseResult, JSON.DEFFAULT_DATE_FORMAT).getBytes(StandardCharsets.UTF_8));
		return serverHttpResponse.writeWith(Flux.just(dataBuffer));
	}
}