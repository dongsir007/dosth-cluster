package com.szbsc.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtUtil {
	
	// token
	public static final String TOKEN = "TOKEN";
	// token秘钥
	public static final String SECRET_KEY = "jwt+shiro";
	// token过期时间 
	public static final long TOKEN_EXPIRE_TIME = 5 * 60 * 1000;
	// token有效时长
	public static final long REFRESH_TOKEN_EXPIRE_TIME = 10 * 60 * 1000;
	// 签发人
	private static final String ISSUSER = "issuser";

	/**
	 * 从token中获取token
	 * @param token
	 * @return
	 */
	public static String getTokenId(String token) {
		try {
			return JWT.decode(token).getClaim(TOKEN).asString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 验证token
	 * @param cacheToken
	 * @return
	 */
	public static boolean verify(String cacheToken) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).withIssuer(ISSUSER).build();
			verifier.verify(cacheToken);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 生成签名
	 * @param token token
	 * @return
	 */
	public static String sign(String token) {
		// jwt的header部分
		Map<String, Object> map = new HashMap<>();
		map.put("alg", "HS256");
		map.put("typ", "JWT");
		
		// 使用jwt的api生成token
		String tokenId = JWT.create()
				.withHeader(map)
				.withClaim(TOKEN, token) // 私有声明
				.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRE_TIME)) // 过期时间
				.withIssuedAt(new Date()) // 签发时间
				.sign(Algorithm.HMAC256(SECRET_KEY)); // 签名
		return tokenId;
	}
}