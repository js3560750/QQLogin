package com.jinsong.core.qq.api;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 所有的API都要继承AbstractOAuth2ApiBinding这样一个抽象类
 * 
 * AbstractOAuth2ApiBinding中有两个变量，一个是accessToken，用来存放令牌，一个是restTemplate，用来发送HTTP请求
 */

public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

	
	//下面这些参数都要参照QQ官方的API文档来开发
	//http://wiki.connect.qq.com/api列表
	private static final String URL_GET_OPENID="https://graph.qq.com/oauth2.0/me?access_token=%s";
	private static final String URL_GET_USERINFO="https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";
	private String appId;
	private String openId;
	
	private ObjectMapper objectMapper = new ObjectMapper();//objectMapper可以帮我们把JSON格式的String转换成我们想要的对象
	
	public QQImpl(String accessToken,String appId) {
		////采用ACCESS_TOKEN_PARAMETER这个策略的时候，父类会自动的把accessToken作为URL的参数传出去
		super(accessToken,TokenStrategy.ACCESS_TOKEN_PARAMETER);
		this.appId=appId;
		
		String url = String.format(URL_GET_OPENID, accessToken);//把accessToken替换掉URL_GET_OPENID里的%s
		String result = getRestTemplate().getForObject(url, String.class);
		
		System.out.println(result);
		
		this.openId=StringUtils.substringBetween(result, "\"openid\":","}");//从result中截出openid的值放入本地变量里
		
	}
	
	@Override
	public QQUserInfo getQQUserInfo()  {
		// TODO Auto-generated method stub
		String url = String.format(URL_GET_USERINFO, appId,openId);
		String result = getRestTemplate().getForObject(url, String.class);
		System.out.println(result);
		//objectMapper可以帮我们把JSON格式的String转换成我们想要的对象
		try {
			return objectMapper.readValue(result, QQUserInfo.class);
		} catch (Exception e) {
			throw new RuntimeException("获取用户信息失败",e);
		} 
		
	}

}
