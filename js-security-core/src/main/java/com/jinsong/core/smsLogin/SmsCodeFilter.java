package com.jinsong.core.smsLogin;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jinsong.core.properties.SecurityProperties;
import com.jinsong.core.validate.code.SmsCode;
import com.jinsong.core.validate.code.ValidateCodeController;
import com.jinsong.core.validate.code.ValidateCodeException;

//继承OncePerRequestFilter保证该过滤器只被调用一次
//我们自定义的短信验证码过滤器，把其加到SpringSecurity的过滤器链上
public class SmsCodeFilter extends OncePerRequestFilter implements InitializingBean{
	
	//失败处理器属性
	private AuthenticationFailureHandler authenticationFailureHandler;
	
	//操作session
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
	
	//访问url
	private Set<String> urls =new HashSet<>();
	
	private SecurityProperties securityProperties;
	
	//String.equal判断的工具类 
	private AntPathMatcher antPathMatcher = new AntPathMatcher();

	public Set<String> getUrls() {
		return urls;
	}

	public void setUrls(Set<String> urls) {
		this.urls = urls;
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}
	
	//需要implements InitializingBean
	//初始化需要验证验证码的url！！！！！！！！！！！！！！！！！！！！！！这里可以看出事短信验证码的URL！！！！！！！！！！！！！！！！
	@Override
	public void afterPropertiesSet() throws ServletException {
		// TODO Auto-generated method stub
		super.afterPropertiesSet();
		String[] configUrls =StringUtils.splitByWholeSeparatorPreserveAllTokens(securityProperties.getSms().getUrl(), ",");
		for(String configUrl:configUrls) {
			urls.add(configUrl);
		}
		urls.add("/authentication/mobile");//短信验证码URL
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//如果是登录请求，则进入if，否则直接调用后面的过滤器
		//if(StringUtils.equals("/authentication/form", request.getRequestURI())	//当请求的URL是/authentication/form并且是POST请求时
		//		&&StringUtils.equalsIgnoreCase(request.getMethod(),"post")) //这里一定要忽略大小写啊！！！！调试了半天，就是没忽略大小写，导致if方法体进不去
		
		//上面两行代码升级改写，判断所有HTTP请求的URL是否属于我们配置的urls，是的话就要进行验证码验证
		boolean flag= false;
		for(String url:urls){
			if(antPathMatcher.match(url, request.getRequestURI())) {
				flag=true;
			}
		}
		
		if(flag)
		{
			
			try {
				//校验验证码的逻辑，验证成功了就往后面走，验证不成功抛异常就不会调用filterChain.doFilter()
				myValidate(new ServletWebRequest(request));
			} catch (ValidateCodeException e) {
				//如果捕获到了异常，用验证失败处理器处理一下
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
				return ;	//直接返回了，因此验证不成功抛异常就不会调用filterChain.doFilter()
			}
		}
		
		//调用过滤器链上后面的过滤器
		filterChain.doFilter(request, response);
		
	}

	//校验验证码的逻辑，短信验证码！！！！！！！！！！！
	private void myValidate(ServletWebRequest request) throws ServletRequestBindingException {
		// TODO Auto-generated method stub
		SmsCode codeInSession = (SmsCode)sessionStrategy.getAttribute(request, ValidateCodeController.SESSION_KEY_SMS);

		String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "smsCode");//拿到前台短信验证码smsCode参数


		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException("验证码的值不能为空");
		}

		if (codeInSession == null) {
			throw new ValidateCodeException("验证码不存在");
		}

		if (codeInSession.isExpried()) {
			sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY_SMS);	//过期了就把验证码从session中移除掉
			throw new ValidateCodeException("验证码已过期");
		}

		if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
			throw new ValidateCodeException("验证码不匹配");
		}

		//上面都过了，就说明验证码验证成功了，不抛异常
		//也把验证码从session中移除掉，因为验证通过了，后面不会再用到
		sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY_SMS);
	}

	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	public SessionStrategy getSessionStrategy() {
		return sessionStrategy;
	}

	public void setSessionStrategy(SessionStrategy sessionStrategy) {
		this.sessionStrategy = sessionStrategy;
	}

	
	
}
