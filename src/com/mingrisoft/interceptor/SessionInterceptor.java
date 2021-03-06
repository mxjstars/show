package com.mingrisoft.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mingrisoft.base.Login;

public class SessionInterceptor extends HandlerInterceptorAdapter {

	Logger logger = Logger.getLogger(SessionInterceptor.class);
	private static final String errorPage = "/index.do";
	

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		System.out.println("SessionInterceptor");
		Login login = (Login) request.getSession().getAttribute("login");
		System.out.println("++++++++++++++++++");
		if (null == login) {
			logger.debug("此用户未登录,禁止访问此URL.");
			response.sendRedirect(request.getContextPath() + errorPage);
			return false;// 如果返回false，则不再调用之后的方法
		}
		return true;
	}

}
