package com.szbsc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description redis-controller
 * @author Zhidong.Guo
 *
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

	private static final Logger logger = LoggerFactory.getLogger(RedisController.class);

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@RequestMapping("/add")
	public String add(@RequestParam("key") String key, @RequestParam("value") Object value) {
		logger.info("add key:" + key + ", value:" + value);
		this.redisTemplate.opsForValue().set(key, value);
		return "add key:" + key + ", value:" + value;
	}
	
	@RequestMapping("read")
	public String read(@RequestParam("key") String key) {
		logger.info("read key:" + this.redisTemplate.opsForValue().get(key));
		return "read key:" + key + ", value:" + this.redisTemplate.opsForValue().get(key);
	}
	
	@RequestMapping("del")
	public String del(@RequestParam("key") String key) {
		logger.info("del key:" + key);
		this.redisTemplate.delete(key);
		return "del key:" + key;
	}
}