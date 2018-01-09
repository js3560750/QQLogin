package com.jinsong.web.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

//注意拦截器要加入到Springboot中生效，还需要额外配置，在com.jinsong.web.config.WebConfig
@Component	
public class TimeInterceptor implements HandlerInterceptor {

	//最后调用，不管有没异常都会调用
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception arg3)
			throws Exception {
		System.out.println("afterCompletion");

	}

	//方法体调用之后调用，但如果方法体抛出了异常，则不调用
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView arg3)
			throws Exception {
		System.out.println("postHandle");
		long startTime = (long) request.getAttribute("startTime");	//获得request的参数
		System.out.println("TimeInterceptor 耗时："+(new Date().getTime()-startTime));

	}

	//方法体调用之前调用
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		System.out.println("preHandle");
		
		request.setAttribute("startTime", new Date().getTime());//request可以存入数据，然后再其他方法体拿出来
		return true;	//返回false则后面的方法体都不会调用
	}

}
