package com.mingrisoft.base;

import java.util.List;
import java.util.Map;

/**
 * session��Ϣģ��
 * 
 * @author dragon
 * 
 */
public class SessionInfo {

	private Login login;

	private Map resourceMap;// �û����Է��ʵ���Դ��ַ�б�


	public Map getResourceMap() {
		return resourceMap;
	}

	public void setResourceMap(Map resourceMap) {
		this.resourceMap = resourceMap;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}



}