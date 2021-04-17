package com.mingrisoft.controller;

import java.io.InputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
	String userName = null;
	String password = null;

	public MyAuthenticator() {
	}

	public MyAuthenticator(String username, String password) {
		this.userName = username;
		this.password = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(userName, password);
	}

	public static void main(String[] args) {
		// 这个类主要是设置邮件
		String str = "test";

		InputStream fis = BaseController.class.getClassLoader().getResourceAsStream("dbinfo.properties");
		Properties pp = new Properties();

		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost(pp.getProperty("mailHost"));// 邮箱接口
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName(pp.getProperty("mailUser"));// 您的邮箱名称
		mailInfo.setPassword(pp.getProperty("mailPwd"));// 您的邮箱密码
		mailInfo.setFromAddress(pp.getProperty("mailUser"));
		mailInfo.setToAddress("");// 目的邮箱
		mailInfo.setSubject("");
		mailInfo.setContent(str);
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendHtmlMail(mailInfo);// 发送html格式
	}
}
