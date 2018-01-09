package com.jinsong.core.properties;

/**
 * 网页登录图片验证码配置
 * @author 188949420@qq.com
 *
 */
public class ImageCodeProperties {
 
	private int width=67;		//验证码图片高度
	private int height = 23;	//验证码图片长度
	private int length = 5;		//验证码位数
	private int expireIn = 60;	//验证码过期时间，60秒过期
	
	private String url="/user";	//限定需要验证验证码的url
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getExpireIn() {
		return expireIn;
	}
	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}
	
}
