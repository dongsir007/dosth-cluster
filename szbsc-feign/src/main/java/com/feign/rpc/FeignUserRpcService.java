package com.feign.rpc;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.feign.dto.FeignUser;

public interface FeignUserRpcService {

	/**
	 * 添加
	 * 
	 * @param feignUser
	 */
	@RequestMapping("/addUser")
	public void addUser(@RequestBody FeignUser feignUser);

	/**
	 * 获取
	 * 
	 * @param feignUser
	 */
	@RequestMapping("/getUser")
	public FeignUser getUser(@RequestBody FeignUser feignUser);
	
	/**
	 * 用户列表
	 */
	@RequestMapping("/list")
	public List<FeignUser> list();
}