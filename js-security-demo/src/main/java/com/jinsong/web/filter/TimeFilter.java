package com.jinsong.web.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.stereotype.Component;


//注意Filter是javax.servlet里的
@Component	//声明为Spring的组件
public class TimeFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("My Time Filter destory");
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		System.out.println("My Time Filter start");
		long startTime =new Date().getTime();
		
		chain.doFilter(request, response);	//过滤器链，调用下一个过滤器
		//下一个指的是下一个filter，如果没有filter那就是你请求的资源。 一般filter都是一个链,web.xml 里面配置了几个就有几个。一个一个的连在一起 
		//request -> filter1 -> filter2 ->filter3 -> .... -> request resource.
		System.out.println("My Time Filter 耗时:"+(new Date().getTime()-startTime));
		System.out.println("My Time Filter finish");
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("My Time Filter Init");
		
	}	

}
