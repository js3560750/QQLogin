package com.jinsong.core.properties;



import com.jinsong.core.qq.config.QQProperties;

//第三方登录
public class SocialProperties {

	private QQProperties qq = new QQProperties();

	public QQProperties getQq() {
		return qq;
	}

	public void setQq(QQProperties qq) {
		this.qq = qq;
	}
	
	
}
