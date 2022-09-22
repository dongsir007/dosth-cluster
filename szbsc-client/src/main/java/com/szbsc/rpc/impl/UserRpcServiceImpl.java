package com.szbsc.rpc.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import com.feign.dto.FeignUser;
import com.szbsc.rpc.UserRpcService;

//@RestController
public class UserRpcServiceImpl implements UserRpcService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserRpcServiceImpl.class);
	
	@Override
	public void addUser(FeignUser feignUser) {
		logger.info("添加用户");
	}

	@Override
	public FeignUser getUser(FeignUser feignUser) {
		logger.info("获取用户");
		return new FeignUser();
	}

	@Override
	public List<FeignUser> list() {
		logger.info("获取用户列表");
		return new ArrayList<>();
	}
}