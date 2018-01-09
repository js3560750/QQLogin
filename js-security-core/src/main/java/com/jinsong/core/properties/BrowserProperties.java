package com.jinsong.core.properties;

public class BrowserProperties {

	//用户在appliaction.properties配了值得话就用配的值，否则就用这个默认值
	//
	private String loginPage="/demo-default.html";	
	
	private int rememberMeSeconds = 3600;	//登录时“记住我”功能的有效时间，这里配置的1小时
	

	public String getLoginPage() {
		return loginPage;
	}

	public void setLoginPage(String loginPage) {
		this.loginPage = loginPage;
	}

	public int getRememberMeSeconds() {
		return rememberMeSeconds;
	}

	public void setRememberMeSeconds(int rememberMeSeconds) {
		this.rememberMeSeconds = rememberMeSeconds;
	}
	
	
}
