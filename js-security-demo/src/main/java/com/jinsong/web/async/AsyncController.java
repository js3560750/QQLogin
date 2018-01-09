package com.jinsong.web.async;



import java.util.concurrent.Callable;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class AsyncController {

	private Logger logger =LoggerFactory.getLogger(getClass());
	
	@Autowired
	MockQueue mockQueue;
	
	@Autowired
	DeferredResultHolder deferredResultHolder;
	
	@RequestMapping("/order")
	public Callable<String> order() throws Exception {
		logger.info("主线程开始");	//日志信息是会打印到控制台里的
		
		//这是开启了一个新的线程！！多线程！！！Callable接口功能和Runnable接口功能很相似
		//两者最大的不同点是：实现Callable接口的任务线程能返回执行结果；而实现Runnable接口的任务线程不能返回结果
		Callable<String> result = new Callable<String>() {

			@Override
			public String call() throws Exception {
				logger.info("副线程开始");	
				Thread.sleep(1000);//睡1秒，假设这部分是订单操作
				logger.info("副线程结束");	
				
				return "success";
			}
		};
		logger.info("主线程结束");
		return result;
	}
	
	@RequestMapping("/orderDeferred")
	public DeferredResult<String> orderDeferred() throws Exception{
		logger.info("主线程开始");
		
		String orderNumer =RandomStringUtils.randomNumeric(8);	//随机生成一个8位的订单号
		mockQueue.setPlaceOrder(orderNumer);	//模拟把订单送入消息队列，mockQueue就是一个模拟的消息队列（虽然不是真的队列），只是为了解释多线程
		
		DeferredResult<String> result = new DeferredResult<>();
		deferredResultHolder.getMap().put(orderNumer, result);
		
		logger.info("主线程结束");
		return result;
		
	}
	
	
	
	
	
	
	
	
}
