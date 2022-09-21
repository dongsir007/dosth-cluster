package com.szbsc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feign.dto.FeignUser;
import com.feign.rpc.FeignUserRpcService;
import com.szbsc.rpc.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private FeignUserRpcService userRepository;
	@Autowired
	private UserService userService;
	
	@RequestMapping("/addUser")
	public String list(@RequestBody FeignUser feignUser) {
		this.userRepository.addUser(feignUser);
		return "添加用户";
	}
	
	@RequestMapping("/getUser")
	public FeignUser getUser(@RequestBody FeignUser feignUser) {
		return this.userRepository.getUser(feignUser);
	}
	
	@RequestMapping("/batchAddUser")
	public String batchAddUser(@RequestBody List<FeignUser> userList) {
		return this.userService.batchAddUser(userList);
	}
}