package com.szbsc.rpc;

import org.springframework.cloud.openfeign.FeignClient;

import com.feign.rpc.FeignUserRpcService;
import com.szbsc.rpc.impl.UserRpcServiceImpl;

@FeignClient(value = "provider-service", fallback = UserRpcServiceImpl.class)
public interface UserRpcService extends FeignUserRpcService {

}