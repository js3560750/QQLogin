package com.jinsong.core.smsLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 短信验证码的配置
 * @author 188949420@qq.com
 *
 */
//注意继承的类，和继承类中的两个泛型
@Component
public class SmsCodeAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>{

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
	
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		//配置SmsCodeAuthenticationFilter 过滤器
		SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();
		smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));	//将过滤器添加到Spring的默认管理类中
		smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);	//添加成功处理器，用的Spring默认的
		smsCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);	//添加失败处理器，用的Spring默认的
		
		//配置SmsCodeAuthenticationPorvider
		SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
		smsCodeAuthenticationProvider.setUserDetailsService(userDetailsService);	//配置这个以读取用户信息
		
		http.authenticationProvider(smsCodeAuthenticationProvider)//将我们自己的Provider添加到Spring管理的Provider集合里
			.addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);	//将我们自己的Filter添加到UsernamePasswordAuthenticationFilter的后面	
		
	}
}
