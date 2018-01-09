package com.jinsong.core.qq.config;

import org.springframework.boot.autoconfigure.social.SocialProperties;

//第三方登录中的QQ登录的配置
public class QQProperties extends SocialProperties {

	private String providerId="qq";

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	
}
