package com.jinsong.security.browser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jinsong.core.properties.SecurityProperties;

@RestController
public class BrowserSecurityController {

	//Spring默认把HTTP请求缓存到了HttpSessionRequestCache这个类里，我们通过RequestCache拿到HTTP请求的缓存
	private RequestCache requestCache =new HttpSessionRequestCache();
	
	//页面跳转的工具
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Autowired
	private SecurityProperties securityProperties;	//js-security-core里面的类
	
	
	
	/*
	 * 当需要身份认证时，跳转到这里
	 */
	@RequestMapping("/authentication/require")
	@ResponseStatus(code=HttpStatus.UNAUTHORIZED)	//函数正常return就返回401状态码。HttpStatus.UNAUTHORIZED=401
	public String requireAutheintication(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//拿到HTTP请求
		SavedRequest savedRequest =requestCache.getRequest(request, response);
		
		if(savedRequest!=null) {
			//拿到引发跳转的URL
			String targetURL = savedRequest.getRedirectUrl();
			//如果URL以.html结尾(忽略大小写)
			if(StringUtils.endsWithIgnoreCase(targetURL, ".html")) {
				//页面跳转，地址是appliaction.properties中的jinsong.security.browser.loginPage配置项
				redirectStrategy.sendRedirect(request, response,securityProperties.getBrowser().getLoginPage());
			}
		}
		
		return "访问的服务需要身份认证，请引导用户到登录页";
	}
}
