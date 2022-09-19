package com.sabsc;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.szbsc.RedisMultResourceApplication.class}, properties = {"application.yml"})
public class RedisMultTest {

	@Autowired
	@Qualifier("redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	@Qualifier("secondaryRedisTemplate")
	private RedisTemplate<String, Object> secondaryRedisTemplate;
	
	@Test
	public void a() {
		System.err.println(this.redisTemplate);
		System.err.println(this.secondaryRedisTemplate);
	}
	
	// 同时设置两个数据源的数据
    @Test
    public void b() {
        System.out.println("同时设置数据");
        this.redisTemplate.opsForValue().set("redis-key2", "redisTemplate");
        System.out.println("over");
    }

    // 同时获取两个数据源的数据
    @Test
    public void c() {
        System.out.println("同时获取数据");
        Object key1 = this.redisTemplate.opsForValue().get("redis-key2");
        System.out.println("key1 = " + key1);
        System.out.println("over");
    }
	
	// 同时设置两个数据源的数据
    @Test
    public void testTemplateSet() {
        System.out.println("同时设置数据");
        this.redisTemplate.opsForValue().set("redis-key2", "redisTemplate");
        this.secondaryRedisTemplate.opsForValue().set("redis-key2", "secondaryRedisTemplate");
        System.out.println("over");
    }

    // 同时获取两个数据源的数据
    @Test
    public void testTemplateGet() {
        System.out.println("同时获取数据");
        Object key1 = this.redisTemplate.opsForValue().get("redis-key2");
        Object key2 = this.secondaryRedisTemplate.opsForValue().get("redis-key2");
        System.out.println("key1 = " + key1);
        System.out.println("key2 = " + key2);
        System.out.println("over");
    }
}