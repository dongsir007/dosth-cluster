package com.szbsc.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import com.szbsc.common.constant.SecurityConstants;
import com.szbsc.common.context.SecurityContextHolder;
import com.szbsc.common.utils.ServletUtils;

/**
 * 自定义请求头拦截器,将Header数据封装到线程变量中获取
 * @author Zhidong.Guo
 *
 */
public class HeaderInterceptor implements AsyncHandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		SecurityContextHolder.setAccountId(ServletUtils.getHeader(request, SecurityConstants.DETAILS_ACCOUNT_ID));
		SecurityContextHolder.setUserName(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_NAME));
		SecurityContextHolder.setUserKey(ServletUtils.getHeader(request, SecurityConstants.DETAILS_USER_KEY));
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		SecurityContextHolder.remove();
	}
}