package com.jinsong.web.async;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component	//实现ApplicationListener<ContextRefreshedEvent>代表Spring框架初始化完成后就加入这个QueueListener监听器
public class QueueListener implements ApplicationListener<ContextRefreshedEvent>{
	
	private Logger logger = LoggerFactory.getLogger(getClass());	//没有new关键字

	@Autowired
	private MockQueue mockQueue;
	
	@Autowired
	private DeferredResultHolder deferredResultHolder;

	//监听器具体内容
	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		
		//开启一个新的线程，注意这个线程的写法！！！！！！！！！！
		new Thread(()-> {
			//用一个while循环一直监听，因为这是一个无限循环，没有退出，所以必须放在单独新开的线程中，否则会阻塞主线程启动Springboot服务
			while(true) {
				//用QueueListener监听这个completeOrder，只要这个completeOrder有值，就返回HTTP响应头给前端
				if(StringUtils.isNotBlank(mockQueue.getCompleteOrder())) //如果mockQueue中completeOrder不为空
				{
					String orderNumber = mockQueue.getCompleteOrder();
					logger.info("返回订单处理结果："+orderNumber);
					deferredResultHolder.getMap().get(orderNumber).setResult("place order success");	//设置deferredResultHolder的键值
					
					mockQueue.setCompleteOrder(null);	//把completeOrder置为null，相当于彻底处理完了这个订单
				}else {
					try {
						Thread.sleep(100);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
		
	}
	
	
}
