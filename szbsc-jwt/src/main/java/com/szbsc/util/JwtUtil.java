package com.szbsc.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.szbsc.config.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Jwt工具类
 * @author Zhidong.Guo
 *
 */
@Component
public class JwtUtil {
	
	private static final String JWT_CACHE_KEY = "jwt:accountId:";
	private static final String ACCOUNT_ID = "accountId";
	private static final String USER_NAME = "userName";
	private static final String ACCESS_TOKEN = "access_token";
	private static final String REFRESH_TOKEN = "refresh_token";
	private static final String EXPIRE_IN = "expire_in";
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	
	@Resource
	private JwtProperties jwtProperties;
	
	/**
	 * 创建Token和RefreshToken
	 * @param accountId
	 * @param userName
	 * @return
	 */
	public Map<String, Object> generateTokenAndRefreshToken(String accountId, String userName) {
		Map<String, Object> tokenMap = this.buildToken(accountId, userName);
		this.cacheToken(accountId, tokenMap);
		return tokenMap;
	}
	
	/**
	 * 刷新并创建token
	 * @param accountId
	 * @param userName
	 * @return
	 */
	public Map<String, Object> refreshTokenAndGenerateToken(String accountId, String userName) {
		Map<String, Object> tokenMap = this.buildToken(accountId, userName);
		this.redisTemplate.delete(JWT_CACHE_KEY + accountId);
		this.cacheToken(accountId, tokenMap);
		return tokenMap;
	}
	
	private Map<String, Object> buildToken(String accountId, String userName) {
		Map<String, Object> tokenMap = new HashMap<>();
		String accessToken = this.generateToken(accountId, userName, null);
		String refreshToken = this.generateRefreshToken(accountId, userName, null);
		tokenMap.put(ACCESS_TOKEN, accessToken);
		tokenMap.put(REFRESH_TOKEN, refreshToken);
		tokenMap.put(EXPIRE_IN, this.jwtProperties.getExpiration());
		return tokenMap;
	}
	
	private void cacheToken(String accountId, Map<String, Object> tokenMap) {
		this.redisTemplate.opsForHash().put(JWT_CACHE_KEY + accountId, ACCESS_TOKEN, tokenMap.get(ACCESS_TOKEN));
		this.redisTemplate.opsForHash().put(JWT_CACHE_KEY + accountId, REFRESH_TOKEN, tokenMap.get(REFRESH_TOKEN));
		this.redisTemplate.expire(accountId, this.jwtProperties.getExpiration() * 2, TimeUnit.MILLISECONDS);
	}

	private String generateToken(String accountId, String userName, Map<String, Object> payloads) {
		Map<String, Object> claims = this.buildClaims(accountId, userName, payloads);
		return this.generateToken(claims);
	}
	
	private String generateRefreshToken(String accountId, String userName, Map<String, Object> payloads) {
		Map<String, Object> claims = this.buildClaims(accountId, userName, payloads);
		return this.generateRefreshToken(claims);
	}

	private Map<String, Object> buildClaims(String accountId, String userName, Map<String, Object> payloads) {
		int payloadSize = payloads == null ? 0 : payloads.size();
		Map<String, Object> claims = new HashMap<>();
		claims.put(Claims.SUBJECT, accountId);
		claims.put(USER_NAME, userName);
		claims.put("created", new Date());
		if (payloadSize > 0) {
			claims.putAll(payloads);
		}
		return claims;
	}
	
	/**
	 * 生成令牌
	 * @param claims 数据声明
	 * @return
	 */
	private String generateToken(Map<String, Object> claims) {
		Date expirationDate = new Date(System.currentTimeMillis() + this.jwtProperties.getExpiration());
		return Jwts.builder().setClaims(claims)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, this.jwtProperties.getSecret())
				.compact();
	}
	
	/**
	 * 生成刷新令牌refreshToken,有效期是令牌的2倍
	 * @param claims
	 * @return
	 */
	private String generateRefreshToken(Map<String, Object> claims) {
		Date expirationDate = new Date(System.currentTimeMillis() + this.jwtProperties.getExpiration() * 2);
		return Jwts.builder().setClaims(claims)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, this.jwtProperties.getSecret())
				.compact();
	}
	
	public String getAccountIdFromRequest(HttpServletRequest request) {
		return request.getHeader(ACCOUNT_ID);
	}
	
	/**
	 * 移除Token
	 * @param accountId
	 * @return true 成功 false 失败
	 */
	public boolean removeToken(String accountId) {
		return Boolean.TRUE.equals(this.redisTemplate.delete(JWT_CACHE_KEY + accountId));
	}
	
	/**
	 * 判断令牌是否不存在redis中
	 * @param token 刷新令牌
	 * @return true 不存在 false 存在
	 */
	public Boolean isRefreshTokenNotExistCache(String token) {
		String accountId = getAccountIdFromToken(token);
		String refreshToken = (String) this.redisTemplate.opsForHash().get(JWT_CACHE_KEY + accountId, REFRESH_TOKEN);
		return refreshToken == null || !refreshToken.equals(token);
	}

	/**
	 * 验证令牌
	 * @param token 令牌
	 * @param accountId 用户Id
	 * @return
	 */
	public Boolean validateToken(String token, String accountId) {
		String tokenAccountId = getAccountIdFromToken(token);
		return (tokenAccountId.equals(accountId) && !isTokenExpired(token));
	}
	
	/**
	 * 判断令牌是否过期
	 * @param token
	 * @return true 已过期 false 未过期
	 */
	public boolean isTokenExpired(String token) {
		try {
			Claims claims = this.getClaimsFromToken(token);
			Date expiration = claims.getExpiration();
			return expiration.before(new Date());
		} catch (Exception e) {
			return true;
		}
	}

	public String getAccountIdFromToken(String token) {
		String accountId = null;
		try {
			Claims claims = this.getClaimsFromToken(token);
			accountId = claims.getSubject();
		} catch (Exception e) {
		}
		return accountId;
	}
	
	/**
	 * 在token中获取用户名
	 * @param token
	 * @return
	 */
	public String getUserNameFromToken(String token) {
		String userName = null;
		try {
			Claims claims = this.getClaimsFromToken(token);
			userName = (String) claims.get(USER_NAME);
		} catch (Exception e) {
		}
		return userName;
	}
	
	/**
	 * 刷新令牌
	 * @param token
	 * @return
	 */
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			Claims claims = this.getClaimsFromToken(token);
			claims.put("created", new Date());
			refreshedToken = this.generateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		
		return refreshedToken;
	}
	
	/**
	 * 从令牌中获取数据声明,验证jwt签名
	 * @param token
	 * @return
	 */
	private Claims getClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(this.jwtProperties.getSecret()).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}
}