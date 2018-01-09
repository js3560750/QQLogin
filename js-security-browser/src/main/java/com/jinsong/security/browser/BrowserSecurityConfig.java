package com.jinsong.security.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.jinsong.core.properties.SecurityProperties;
import com.jinsong.core.smsLogin.SmsCodeAuthenticationConfig;
import com.jinsong.core.smsLogin.SmsCodeAuthenticationFilter;
import com.jinsong.core.smsLogin.SmsCodeFilter;
import com.jinsong.core.validate.code.ValidateCodeFilter;

@Configuration	//要继承WebSecurityConfigurerAdapter
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private SecurityProperties securityProperties;
	
	//自定义的登录成功的处理器 /jinsong/security/browser/authentication/JinsongAuthenticationSuccessHandler.java
	@Autowired
	private AuthenticationSuccessHandler myAuthenticationSuccessHandler;
	
	//自定义IDE登录失败的处理器 /jinsong/security/browser/authentication/JinsongAuthenticationFailureHandler.java
	@Autowired
	private AuthenticationFailureHandler myAuthenticationFailureHandler;
	
	//数据库
	//数据库的配置就是application.properties里写的本地security数据库
	@Autowired
	private DataSource dataSource;
	
	//SpringSecurity的“记住我”功能
	//这里配置一个Token，相当于用户的身份标识
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl jdbcTokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		jdbcTokenRepositoryImpl.setDataSource(dataSource);
		//在第一次启动的时候在数据库中创建表persistent_logins 以存放token信息
		//之后就要把这句注释掉，其实可以F3 JdbcTokenRepositoryImpl这个类去找建表的SQL的代码，自己建表，然后也就不需要下面这一句了
		//jdbcTokenRepositoryImpl.setCreateTableOnStartup(true);	
		return jdbcTokenRepositoryImpl;
	}
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private SmsCodeAuthenticationConfig smsCodeAuthenticationConfig;
	
	//配置
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//图形验证码过滤器
		ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
		validateCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);	//设置该过滤器的失败处理器为我们自定义的myAuthenticationFailureHandler
		validateCodeFilter.setSecurityProperties(securityProperties);	//把securityProperties传入validateCodeFilter里
		validateCodeFilter.afterPropertiesSet();	//调用afterPropertiesSet方法初始化urls
		
		//短信验证码过滤
		SmsCodeFilter smsCodeFilter = new SmsCodeFilter();
		smsCodeFilter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);	//设置该过滤器的失败处理器为我们自定义的myAuthenticationFailureHandler
		smsCodeFilter.setSecurityProperties(securityProperties);	//把securityProperties传入validateCodeFilter里
		smsCodeFilter.afterPropertiesSet();
		
		//表单登录，这个可以做的很复杂
		http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)	//把自定义的验证码过滤器validateCodeFilter加到Spring默认过滤器UsernamePasswordAuthenticationFilter的前面
			.addFilterBefore(smsCodeFilter, UsernamePasswordAuthenticationFilter.class)
			.formLogin()
			//.loginPage("/signIn.html")//登录访问的页面，HTML页面必须保存在src/main/resources/resources/下面
			.loginPage("/authentication/require")//登录访问的URL，该URL的控制器在BrowserSecurityController里面
			.loginProcessingUrl("/authentication/form")	//配置登录表单提交到的URL
			.successHandler(myAuthenticationSuccessHandler)//配置我们自定义的登录成功处理器
			.failureHandler(myAuthenticationFailureHandler)//配置我们自定义的登录失败处理器
			.and()
			.rememberMe()	//登录时“记住我”功能配置
			.tokenRepository(persistentTokenRepository())//token配置
			.tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())//token过期时间配置
			.userDetailsService(userDetailsService)	//从token中拿到用户名之后，用userDetailsService去做登录
			.and()
			.authorizeRequests()
			//.antMatchers("/signIn.html").permitAll()	//该页面不需要验证，登录页面都没登录进去，当然不需要验证
			.antMatchers("/authentication/require",
					securityProperties.getBrowser().getLoginPage()
					,"/code/*").permitAll()	//"/code/*"短信和图片验证码的调用也要未登录允许访问
			.anyRequest()	//所有请求
			.authenticated()
			.and()
			.csrf().disable()	//关闭CSRF防护，因为没有做配置，不关闭的话登录提交会报错
			.apply(smsCodeAuthenticationConfig);	//将smsCodeAuthenticationConfig类里写的配置加到浏览器的配置里
		
		//默认的弹窗登录
		/*
		http.httpBasic()
			.and()
			.authorizeRequests()
			.anyRequest()
			.authenticated();
		*/
	}
	
	//配置
	//账户密码加密，这里的PasswordEncoder用org.springframework.security.crypto.这个包下的
		//开启加密
		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}
}
















