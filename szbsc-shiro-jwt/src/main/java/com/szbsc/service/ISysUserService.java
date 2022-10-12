package com.szbsc.service;

import java.util.Set;

import com.szbsc.entity.SysUser;

public interface ISysUserService {

	Set<String> getUserRolesSet(String userName);

	Set<String> getUserPermissionsSet(String userName);

	SysUser getUserByName(String userName);

}