package com.szbsc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.szbsc.common.constant.SecurityConstants;

import reactor.core.publisher.Mono;

@Component
public class GlobalLoginFilter implements GlobalFilter, Ordered {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalLoginFilter.class);
	
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpRequest request = exchange.getRequest();
		
		logger.info(request.getURI().getPath());
		String token = request.getHeaders().getFirst(SecurityConstants.AUTHORIZATION_HAEDER);
		
		logger.info("token:" + token);
//		exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//		return exchange.getResponse().setComplete();
		
		
		return chain.filter(exchange);
	}
}