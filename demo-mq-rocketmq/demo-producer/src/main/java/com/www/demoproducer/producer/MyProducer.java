package com.www.demoproducer.producer;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @Describtion: MyProducer
 * @Author: 张卫刚
 * @Date: 2024/8/10 21:01
 */
@Service
public class MyProducer {
	private Logger logger = LoggerFactory.getLogger(MyProducer.class);

	@Autowired
	private RocketMQTemplate rocketmqTemplate;

	private final String topic = "rocketmqTest";

	public void send() {
		Message<String> msg = MessageBuilder.withPayload("Hello," ).build();
		rocketmqTemplate.send(topic, msg);
	}

	public void sendTest() {
		send();
		testTransaction();
	}

	public void testTransaction() throws MessagingException {
		String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};
		for (int i = 0; i < 10; i++) {
			try {

				Message<String> msg = MessageBuilder.withPayload("Hello RocketMQ " + i).
						setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_" + i).build();
				SendResult sendResult = rocketmqTemplate.sendMessageInTransaction(
						topic + ":" + tags[i % tags.length], msg, null);
				System.out.printf("------ send Transactional msg body = %s , sendResult=%s %n",
						msg.getPayload(), sendResult.getSendStatus());

				Thread.sleep(10);
			} catch (Exception e) {
				logger.error("发送事务消息失败", e);
			}
		}
	}
}
