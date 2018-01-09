package com.jinsong.core.qq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

import com.jinsong.core.properties.SecurityProperties;
import com.jinsong.core.qq.connect.QQConnectionFactory;

@Configuration
@ConditionalOnProperty(prefix="jinsong.security.social.qq",name="app-id")//意思是在application.properties中有这配置，下面的代码才生效
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

	@Autowired
	private SecurityProperties securityProperties;
	
	@Override
	protected ConnectionFactory<?> createConnectionFactory() {
		QQProperties qq = securityProperties.getSocial().getQq();
		return new QQConnectionFactory(qq.getProviderId(), qq.getAppId(), qq.getAppSecret());
	}

}
