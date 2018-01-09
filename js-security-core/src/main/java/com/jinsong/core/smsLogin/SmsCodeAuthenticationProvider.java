package com.jinsong.core.smsLogin;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
	
	private UserDetailsService userDetailsService;

	//短信登录身份认证的逻辑写在这
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken)authentication;
		UserDetails user = userDetailsService.loadUserByUsername((String)authenticationToken.getPrincipal());//Principal是手机号，前台默认传的是18571686931
		
		if(user==null) {
			throw new InternalAuthenticationServiceException("无法获取用户信息");
		}
		
		SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());	//这里用两个参数的SmsCodeAuthenticationToken构造函数
		
		authenticationResult.setDetails(authenticationToken.getDetails());//把认证结果放到authenticationResult
		
		return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// 判断传进来的authentication是否是SmsCodeAuthenticationToken这种类型的
		return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public UserDetailsService getUserDetailsService() {
		return userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	
	
}
