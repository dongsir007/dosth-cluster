package com.szbsc.rpc.impl;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;

import com.feign.dto.FeignUser;
import com.feign.rpc.FeignUserRpcService;

@FeignClient("")
public class FeignUserRpcServiceImpl implements FeignUserRpcService {

	@Override
	public void addUser(FeignUser feignUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FeignUser getUser(FeignUser feignUser) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FeignUser> list() {
		// TODO Auto-generated method stub
		return null;
	}
}