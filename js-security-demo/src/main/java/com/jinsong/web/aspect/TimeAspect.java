package com.jinsong.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect //这个注解需要在pom.xml添加spring-boot-starter-aop依赖
@Component
public class TimeAspect {

	//什么时候调用切片，即声明切入点，通过注解决定，有@Before，@After,@AfterThrowing和@Around
	//一般直接用@Around
	//都是org.aspectj.lang.annotation下面的
	@Around("execution(* com.jinsong.web.controller.UserController.*(..))")	//@Around里的内容声明切片在哪些方法上起作用，这里声明UserController下的所有方法都起作用
	public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("TimeAspect start"); 
		
		Object[] args=pjp.getArgs();	//获得方法体的参数
		for(Object arg:args) {
			System.out.println("arg is "+arg);
		
		}
		
		//!!!!!!!!!!!!!!!!!!!!!!这一句非常重要，这一句是执行方法体！！！！！！！！！！！！！！！！
		Object obj=pjp.proceed();	//一定要返回obj，不然页面显示为null
		System.out.println("TimeAspect end");
		return obj;
	}
}
