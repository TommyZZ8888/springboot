package com.www.democonsumer.consumer;


import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * @Describtion: MyConsumer
 * @Author: 张卫刚
 * @Date: 2024/8/10 21:01
 */
@Service
@RocketMQMessageListener(topic = "rocketmqTest", consumerGroup = "rock")
public class MyConsumer implements RocketMQListener<String> {

	@Override
	public void onMessage(String message) {
		// 处理消息的逻辑
		System.out.println("Received message: " + message);
	}
}
