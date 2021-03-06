package com.mingrisoft.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.mingrisoft.base.Login;
import com.mingrisoft.controller.BaseController;



public class SpringMVCInterceptor extends HandlerInterceptorAdapter  {
	
	@Override
	public boolean preHandle(HttpServletRequest request,
	HttpServletResponse response, Object handler) throws Exception {
		
		  System.out.println("SpringMVCInterceptor");
	  try{
		  BaseController bc = (BaseController)handler ;
		  bc.setRequest(request);
		  bc.setResponse(response);
		  bc.setSession(request.getSession());
		  bc.setLogin((Login)request.getSession().getAttribute("login"));
	  }	catch(Exception e){
		  
		  e.printStackTrace();
	  }
	  return super.preHandle(request, response, handler);
	  
	}



}
