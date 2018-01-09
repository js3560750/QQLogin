package com.jinsong.core.qq.config;

import javax.sql.DataSource;

import org.apache.catalina.valves.JDBCAccessLogValve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableSocial
public class QQConfig extends SocialConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		
		/*
		 * dataSource数据源
		 * connectionFactoryLocator自动确定是哪个第三方的connectionFactory
		 * Encryptors是一个加解密的工具，这里的Encryptors.noOpText()是不做任何操作
		 */
		JdbcUsersConnectionRepository jdbcRepository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
		jdbcRepository.setTablePrefix("my_");	//设置表前缀，因此数据库中对应的表名是my_userconnection
		return jdbcRepository;
	}
	
	@Bean
	public SpringSocialConfigurer jinsongSocialSecurityConfig() {
		return new SpringSocialConfigurer();
	}
	
	
	
	
	
	
	
	
}
