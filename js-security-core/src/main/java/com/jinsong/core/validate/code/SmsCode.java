package com.jinsong.core.validate.code;


import java.time.LocalDateTime;

//短信验证码
public class SmsCode {

	
	
	private String code;	//验证码
	
	private LocalDateTime expireTime;	//过期时间
	
	public SmsCode(String code, int expireIn) {
		super();
		
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);	//过期时间等于当前时间+传入的expireIn秒数
	}

	public SmsCode(String code, LocalDateTime expireTime) {
		super();
		
		this.code = code;
		this.expireTime = expireTime;
	}

	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
	}

	//判断是否过期
	public boolean isExpried() {
		// TODO Auto-generated method stub
		//当前时间若在过期时间之后，就返回true，就说明过期了
		return LocalDateTime.now().isAfter(expireTime);
	}
	
	
}
