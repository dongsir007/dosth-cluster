package com.szbsc.config;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.szbsc.entity.JwtToken;
import com.szbsc.entity.SysUser;
import com.szbsc.service.ISysUserService;
import com.szbsc.util.JwtUtil;

@Component
public class ShiroRealm extends AuthorizingRealm {
	
	@Autowired
	private ISysUserService sysUserService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JwtToken;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUser sysUser = null;
		String userName = null;
		if (principals != null) {
			sysUser = (SysUser) principals.getPrimaryPrincipal();
			userName = sysUser.getUserName();
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		
		Set<String> roleSet = this.sysUserService.getUserRolesSet(userName);
		info.setRoles(roleSet);
		
		Set<String> permissionSet = this.sysUserService.getUserPermissionsSet(userName);
		info.addStringPermissions(permissionSet);
		
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String jwtToken = (String) token.getPrincipal();
		String userName = null;
		try {
			userName = JwtUtil.getUserName(jwtToken);
		} catch (Exception e) {
			throw new AuthenticationException("token非法或过期");
		}
		if (!JwtUtil.verify(jwtToken) || userName == null) {
			throw new AuthenticationException("token认证失效,token错误或过期,重新登录");
		}
		SysUser loginUser = this.sysUserService.getUserByName(userName);
		return new SimpleAuthenticationInfo(loginUser, jwtToken, getName());
	}
}