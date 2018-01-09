package com.jinsong.web.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//模拟的消息队列
@Component
public class MockQueue {
	
	private Logger logger = LoggerFactory.getLogger(getClass());	//没有new关键字

	private String placeOrder;	//未处理的订单
	
	private String completeOrder;	//已处理的订单
	

	public String getPlaceOrder() {
		return placeOrder;
	}

	public void setPlaceOrder(String placeOrder) throws Exception {
		//注意新开线程的写法！！！！！！！
		new Thread(()->{
			
			logger.info("接到下单请求 ，"+placeOrder);
			try {
				Thread.sleep(1000);//模拟订单处理
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//注意这里把传进来的placeOrder赋给了completeOrder，相当于未处理订单变成已处理订单
			//然后要把这个已处理订单的信息通知到前端
			//用QueueListener监听这个completeOrder，只要这个completeOrder有值，就返回HTTP响应头给前端
			this.completeOrder = placeOrder;	
			logger.info("下单请求处理完毕，"+placeOrder);
			
		}).start();
		
	}

	public String getCompleteOrder() {
		return completeOrder;
	}

	public void setCompleteOrder(String completeOrder) {
		this.completeOrder = completeOrder;
	}
	
	
}
