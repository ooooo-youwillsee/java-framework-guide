package com.ooooo.test;

import com.alibaba.fastjson.JSON;
import com.ooooo.annotation.ISimpleListener;
import com.ooooo.entity.Message;
import com.ooooo.entity.Order;
import com.ooooo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author leizhijie
 * @since 2021/2/23 17:22
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class TestISimpleListenerInterface {
	
	
	@Component
	public static class UserMessageISimpleListener implements ISimpleListener<Message<User>> {
		
		@Override
		public void invoke(Message<User> msg) {
			log.info("UserMessageISimpleListener : {}", JSON.toJSONString(msg));
		}
	}
	
	@Component
	public static class UserISimpleListener implements ISimpleListener<User> {
		
		@Override
		public void invoke(User msg) {
			log.info("UserISimpleListener : {}", JSON.toJSONString(msg));
		}
	}
	
	@Component
	public static class OrderMessageISimpleListener implements ISimpleListener<Message<Order>> {
		
		@Override
		public void invoke(Message<Order> msg) {
			log.info("OrderMessageISimpleListener : {}", JSON.toJSONString(msg));
		}
	}
	
	@Component
	public static class OrderISimpleListener implements ISimpleListener<Order> {
		
		@Override
		public void invoke(Order msg) {
			log.info("OrderISimpleListener : {}", JSON.toJSONString(msg));
		}
	}
	
	
	@Component
	public static class NoGenericSimpleListener implements ISimpleListener {
		
		@Override
		public void invoke(Object msg) {
			log.info("NoGenericSimpleListener : {}", JSON.toJSONString(msg));
		}
	}
}
