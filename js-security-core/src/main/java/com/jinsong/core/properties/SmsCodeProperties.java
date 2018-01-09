package com.jinsong.core.properties;

/**
 * 短信验证码属性
 * 
 * @author 188949420@qq.com
 *
 */
public class SmsCodeProperties {

	private int length = 6;
	private int expireIn = 60;
	
	private String url="/user";	//限定需要验证验证码的url

	public int getLength() {
		return length;
	}
	public void setLength(int lenght) {
		this.length = lenght;
	}
	public int getExpireIn() {
		return expireIn;
	}
	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}
