package com.mingrisoft.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;

import com.mingrisoft.base.AbstractEntity;
import com.mingrisoft.base.Login;
import com.mingrisoft.base.SessionInfo;
import com.mingrisoft.base.ConfigUtil;




@Controller
public abstract class BaseController {
	
	
	//分页查询对象的参数，每页行数
	protected int rowPerPage = 10;
	
	protected List colRs;
	
	protected HttpServletRequest request ; 

	protected HttpServletResponse response ;

	protected HttpSession session ;
	
	protected Login login ;
	
	protected String sysDeptCode;
	
	
	public List getColRs() {
		return colRs;
	}



	public void setColRs(List colRs) {
		this.colRs = colRs;
	}

	public void  init(AbstractEntity ae){
		SessionInfo sessionInfo = (SessionInfo) session.getAttribute(ConfigUtil.getSessionInfoName());
		colRs = (List) sessionInfo.getResourceMap().get(ae.getMenuId());//获得该功能的功能按钮权限 rs.get(0) 为按钮权限  rs.get(1) 为按钮权限 列表权限
	}
	
	
	public HttpServletResponse getResponse() {
		return response;
	}



	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}



	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}



	public HttpSession getSession() {
		return session;
	}



	public void setSession(HttpSession session) {
		this.session = session;
	}
	

	protected Login getLoginSession(){
		return (Login)session.getAttribute("login");
	}


	public Login getLogin() {
		return login;
	}



	public void setLogin(Login login) {
		this.login = login;
	}


}
