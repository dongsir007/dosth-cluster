package com.szbsc.rpc;

import java.util.List;

import com.feign.dto.FeignUser;

public interface UserService {
	/**
	 * 批量添加用户
	 * @param userList
	 * @return
	 */
	public String batchAddUser(List<FeignUser> userList);
}