package com.jinsong.core.validate.code;

import org.springframework.stereotype.Component;

@Component
public class SmsCodeSender {

	public void send(String mobile,String code) {
		
		//这里应该用短信服务商发送短信验证码到用户手机上
		//用了飞鸽传书公司的服务，经过测试，是OK的，具体可以看OneNote笔记
		//现在只打印到控制台上，模拟一下服务
		System.out.println("向手机"+mobile+"发送验证码"+code);
	}
}
