package com.jinsong.core.qq.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Template;

import com.jinsong.core.qq.api.QQ;
import com.jinsong.core.qq.api.QQImpl;

/*
 * AbstractOAuth2ServiceProvider后面的泛型声明我们的第三方API接口类型，这里是QQ
 * 
 * 
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {
	
	private String appId;
	
	private static final String URL_AUTHORIZE="https://graph.qq.com/oauth2.0/authorize";	//获取授权码的请求地址，查看QQ互联文档说明
	
	private static final String URL_ACCESS_TOKEN="https://graph.qq.com/oauth2.0/token";	//获取令牌的请求地址，查看QQ互联文档说明

	/*appId 去QQ互联上注册，QQ官方给我们的app账号
	 * appSecret QQ官方给我们的app密码
	 */
	public QQServiceProvider(String appId,String appSecret) {
		super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public QQ getApi(String accessToken) {
		// TODO Auto-generated method stub
		return new QQImpl(accessToken, appId);
	}

}
