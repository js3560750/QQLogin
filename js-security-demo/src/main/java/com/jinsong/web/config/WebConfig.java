package com.jinsong.web.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.jinsong.web.interceptor.TimeInterceptor;



@Configuration	//声明这是一个spring配置设置
public class WebConfig  extends WebMvcConfigurerAdapter{

	@Autowired
	private TimeInterceptor timeInterceptor;
	
	//异步的时候拦截器注册
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		//用以下方法注册拦截器
		//configurer.registerCallableInterceptors(interceptor);	//Callable模式
		//configurer.registerDeferredResultInterceptors(interceptor);	//Deferred模式
		
		//设置超时时间，如果异步的线程阻塞了，超时就会返回
		//configurer.setDefaultTimeout(timeout)
		
		//设置自建的线程池来替代异步时Spring默认的线程池
		//configurer.setTaskExecutor(taskExecutor)
	}
	
	//同步时
	//将我们自定义的拦截器加入到Spring
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(timeInterceptor);
	}
}
