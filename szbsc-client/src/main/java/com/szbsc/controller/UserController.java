package com.szbsc.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.feign.dto.FeignUser;
import com.szbsc.rpc.UserRpcService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserRpcService userRpcService;
	
	@RequestMapping("/list")
	public List<FeignUser> list() {
		return this.userRpcService.list();
	}
}