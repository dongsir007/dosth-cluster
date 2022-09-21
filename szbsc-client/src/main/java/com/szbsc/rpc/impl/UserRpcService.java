package com.szbsc.rpc.impl;

import org.springframework.cloud.openfeign.FeignClient;

import com.feign.rpc.FeignUserRpcService;

@FeignClient("")
public interface UserRpcService extends FeignUserRpcService {

}