package com.szbsc.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

/**
 * 多集群数据源配置
 * 
 * @author Zhidong.Guo
 *
 */
@Configuration
public class RedisConfig {

	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	@Autowired
	private Environment environment;

	/**
	 * 配置lettuce连接池
	 */
	@Bean
	@Primary
	@ConfigurationProperties(prefix = "spring.redis.cluster.lettuce.pool")
	public GenericObjectPoolConfig<?> redisPool() {
		logger.info("配置lettuce连接池");
		return new GenericObjectPoolConfig<>();
	}

	/**
	 * 配置第一个数据源
	 */
	@Bean("redisClusterConfig")
	@Primary
	public RedisClusterConfiguration redisClusterConfig() {
		logger.info("");
		Map<String, Object> source = new HashMap<>();
		source.put("spring.redis.cluster.nodes", this.environment.getProperty("spring.redis.cluster.nodes"));
		RedisClusterConfiguration configuration = new RedisClusterConfiguration(
				new MapPropertySource("RedisClusterConfiguration", source));
		configuration.setPassword(this.environment.getProperty("spring.redis.password"));
		return configuration;
	}
	
	/**
	 * 配置第二个数据源
	 */
	@Bean("secondaryRedisClusterConfig")
	public RedisClusterConfiguration secondaryRedisClusterConfig() {
		logger.info("");
		Map<String, Object> source = new HashMap<>();
		source.put("spring.redis.cluster.nodes", environment.getProperty("spring.secondaryRedis.cluster.nodes"));
		RedisClusterConfiguration configuration = new RedisClusterConfiguration(
				new MapPropertySource("RedisClusterConfiguration", source));
		configuration.setPassword(environment.getProperty("spring.secondaryRedis.password"));
		return configuration;
	}

	/**
	 * 配置第一个数据源连接工厂
	 * 
	 * @param redisPool
	 * @param redisClusterConfig
	 * @return
	 */
	@Bean("lettuceConnectionFactory")
	@Primary
	public LettuceConnectionFactory lettuceConnectionFactory(GenericObjectPoolConfig<?> redisPool,
			@Qualifier("redisClusterConfig") RedisClusterConfiguration redisClusterConfig) {
		logger.info("");
		LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
				.poolConfig(redisPool).build();
		return new LettuceConnectionFactory(redisClusterConfig, clientConfiguration);
	}
	
	/**
	 * 配置第二个数据源工厂
	 * @param redisPool
	 * @param secondaryRedisClusterConfig
	 * @return
	 */
	@Bean("secondaryLettuceConnenctionFactory")
	public LettuceConnectionFactory sencondaryLettuceConnenction(GenericObjectPoolConfig<?> redisPool,
			@Qualifier("secondaryRedisClusterConfig") RedisClusterConfiguration secondaryRedisClusterConfig) {
		logger.info("");
		LettuceClientConfiguration clientConfiguration = LettucePoolingClientConfiguration.builder()
				.poolConfig(redisPool).build();
		return new LettuceConnectionFactory(secondaryRedisClusterConfig, clientConfiguration);
	}

	/**
	 * 创建第一个数据源的redisTemplate
	 * 
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean("redisTemplate")
	@Primary
	public RedisTemplate<String, Object> redisTemplate(
			@Qualifier("lettuceConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
		logger.info("创建第一个数据源的redisTemplate");
		return this.getRedisTemplate(redisConnectionFactory);
	}
	
	/**
	 * 创建第二个数据源的redisTemplate
	 * 
	 * @param secondaryLettuceConnenctionFactory
	 * @return
	 */
	@Bean("secondaryRedisTemplate")
	public RedisTemplate<String, Object> secondaryRedisTemplate(
			@Qualifier("secondaryLettuceConnenctionFactory") RedisConnectionFactory secondaryLettuceConnenctionFactory) {
		logger.info("创建第二个数据源的redisTemplate");
		return this.getRedisTemplate(secondaryLettuceConnenctionFactory);
	}

	/**
	 * 
	 * @param factory获取RedisTemplate
	 * @return
	 */
	private RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
		om.activateDefaultTyping(om.getPolymorphicTypeValidator(), DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.WRAPPER_ARRAY);
		jackson2JsonRedisSerializer.setObjectMapper(om);

		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setHashKeySerializer(stringRedisSerializer);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);

		return template;
	}
}