package com.szbsc.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

@Configuration
public class RedisConfig {
	
//    @Value("${spring.redis.cluster.nodes}")
//    private String nodes;
//    @Value("${spring.redis.cluster.max-redirects:3}")
//    private Integer maxRedirects;
	
//	@Value("${spring.redis.host}")
//	private String host;
//	@Value("${spring.redis.port}")
//	private int port;
//    @Value("${spring.redis.password:}")
//    private String password;
//    @Value("${spring.redis.database:0}")
//    private Integer database;
//
//    @Value("${spring.redis.jedis.pool.max-active:8}")
//    private Integer maxActive;
//    @Value("${spring.redis.jedis.pool.max-idle:8}")
//    private Integer maxIdle;
//    @Value("${spring.redis.jedis.pool.max-wait:-1}")
//    private Long maxWait;
//    @Value("${spring.redis.jedis.pool.min-idle:0}")
//    private Integer minIdle;
	
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
//		RedisTemplate<String, Object> template = new RedisTemplate<>();
//		template.setConnectionFactory(factory);
//		
//		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//		ObjectMapper om = new ObjectMapper();
//		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
//		serializer.setObjectMapper(om);
//		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//		template.setKeySerializer(stringSerializer);
//		template.setHashKeySerializer(stringSerializer);
//		
//		template.setValueSerializer(serializer);
//		template.setHashValueSerializer(serializer);
//		
//		template.afterPropertiesSet();
//		return template;
//	}
	
//	@Bean
//    public RedisConnectionFactory redisConnectionFactory(
//    		JedisPoolConfig jedisPool,
//            RedisClusterConfiguration 
////            RedisStandaloneConfiguration
//            jedisConfig
//            
//    		) {
//        JedisConnectionFactory factory = new JedisConnectionFactory(jedisConfig 
//        		, jedisPool
//        		);
//        factory.afterPropertiesSet();
//        return factory;
//    }

//    @Bean
//    public JedisPoolConfig jedisPool() {
//        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(this.maxIdle);
//        jedisPoolConfig.setMaxWaitMillis(this.maxWait);
//        jedisPoolConfig.setMaxTotal(this.maxActive);
//        jedisPoolConfig.setMinIdle(this.minIdle);
//        
//        return jedisPoolConfig;
//    }

//    @Bean
//    public 
//     RedisClusterConfiguration 
////    	RedisStandaloneConfiguration 
//    jedisConfig() {
//        RedisClusterConfiguration config = new RedisClusterConfiguration();
//
//        String[] sub = nodes.split(",");
//        List<RedisNode> nodeList = new ArrayList<>(sub.length);
//        String[] tmp;
//        for (String s : sub) {
//            tmp = s.split(":");
//            // fixme 先不考虑异常配置的case
//            nodeList.add(new RedisNode(tmp[0], Integer.valueOf(tmp[1])));
//        }
//    	  config.setClusterNodes(nodeList);
//        config.setMaxRedirects(this.maxRedirects);
//        config.setPassword(this.password);
//
////    	RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
////        config.setHostName(this.host);
////        config.setPort(this.port);
////        config.setPassword(this.password);
//    	
//        return config;
//    }
	
	@Resource
	private LettuceConnectionFactory lettuceConnectionFactory;
	
	@Bean
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);
		
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
		serializer.setObjectMapper(om);
		RedisSerializer<String> stringSerializer = new StringRedisSerializer();
		template.setKeySerializer(stringSerializer);
		template.setHashKeySerializer(stringSerializer);
		
		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);
		
		template.afterPropertiesSet();
		return template;
	}
}