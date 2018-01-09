package com.jinsong.security.browser.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinsong.core.properties.SecurityProperties;

//自定义的登录成功处理器
//一定要实现AuthenticationSuccessHandler接口
@Component("myAuthenticationSuccessHandler")	//给这个组件起名字
public class JinsongAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//把Authentication转成JSON格式
	@Autowired
	private ObjectMapper objectMapper;	//Spring启动时会自动帮我们注册一个ObjectMapper
	

	
	//登录成功所调用的方法
	//authentication对象包含登录成功的用户信息
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		logger.info("登录成功");
		
		//下面两句的意思是
		//把authentication通过ObjectMapper写成字符串
		//然后通过response以application/json的形式写回我们的响应
		//最终把authentication转成了JSON格式
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(authentication));
		
	}

}
