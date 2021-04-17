package com.mingrisoft.controller;

import java.io.InputStream;
import java.util.Properties;

public class SendMailTool {
	
	public static boolean sendMail(String toAddress,String subject,String content){
		
		InputStream fis = BaseController.class.getClassLoader().getResourceAsStream("dbinfo.properties");
		Properties pp = pp = new Properties();
		
		MailSenderInfo mailInfo = new MailSenderInfo();   
		mailInfo.setMailServerHost(pp.getProperty("mailHost"));   
		mailInfo.setMailServerPort("25");   
		mailInfo.setValidate(true);   
		mailInfo.setUserName(pp.getProperty("mailUser"));   
		mailInfo.setPassword(pp.getProperty("mailPwd"));//您的邮箱密码   
		mailInfo.setFromAddress(pp.getProperty("mailUser"));   
		mailInfo.setToAddress(toAddress);   
		mailInfo.setSubject(subject);   
		mailInfo.setContent(content);   
		SimpleMailSender sms = new SimpleMailSender();  
		// sms.sendTextMail(mailInfo);//发送文体格式   
		boolean flage = sms.sendHtmlMail(mailInfo);//发送html格式  
		return flage;
		
	}
	
	public static String getSendTemp(String content,String title){
		
		if(content.equals("")){
			
			content = "test";
		}
		
		String str ="test"+content; 
		
		return str;
	}
	
}
