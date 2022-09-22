package com.szbsc.rpc;

import org.springframework.cloud.openfeign.FeignClient;

import com.feign.rpc.FeignUserRpcService;

@FeignClient(value = "provider-service")
public interface UserRpcService extends FeignUserRpcService {

}