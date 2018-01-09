package com.jinsong.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

//会读取配置文件appliaction.properties中所有以jinsong.security开头的配置项
//要在com.jinsong.core.SecurityCoreConfig中启动这个配置
@ConfigurationProperties(prefix="jinsong.security")
public class SecurityProperties {

	//以jinsong.security.browser开头的配置都会读取进这个对象里
	private BrowserProperties browser = new BrowserProperties();
	
	//以jinsong.security.image开头的配置都会读取进这个对象里
	private ImageCodeProperties image = new ImageCodeProperties();
	
	private SmsCodeProperties sms =new SmsCodeProperties();
	
	//第三方登录
	private SocialProperties social = new SocialProperties();

	public SocialProperties getSocial() {
		return social;
	}

	public void setSocial(SocialProperties social) {
		this.social = social;
	}

	public SmsCodeProperties getSms() {
		return sms;
	}

	public void setSms(SmsCodeProperties sms) {
		this.sms = sms;
	}

	public BrowserProperties getBrowser() {
		return browser;
	}

	public void setBrowser(BrowserProperties browser) {
		this.browser = browser;
	}

	public ImageCodeProperties getImage() {
		return image;
	}

	public void setImage(ImageCodeProperties image) {
		this.image = image;
	}

	
	
}
