package com.ooooo.test;

import com.ooooo.annotation.ISimpleListener;
import com.ooooo.dao.entity.Message;
import com.ooooo.dao.entity.Order;
import com.ooooo.dao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/23 17:22
 */
@Slf4j
@Configuration(proxyBeanMethods = false)
public class TestISimpleListenerInterface {
	
	
	@Component
	public static class UserMessageISimpleListener implements ISimpleListener<Message<User>> {
		
		@Override
		public void invoke(Message<User> msg) {
			log.info("UserMessageISimpleListener : {}", msg);
		}
	}
	
	@Component
	public static class UserISimpleListener implements ISimpleListener<User> {
		
		@Override
		public void invoke(User msg) {
			log.info("UserISimpleListener : {}", msg);
		}
	}
	
	@Component
	public static class OrderMessageISimpleListener implements ISimpleListener<Message<Order>> {
		
		@Override
		public void invoke(Message<Order> msg) {
			log.info("OrderMessageISimpleListener : {}", msg);
		}
	}
	
	@Component
	public static class OrderISimpleListener implements ISimpleListener<Order> {
		
		@Override
		public void invoke(Order msg) {
			log.info("OrderISimpleListener : {}", msg);
		}
	}
	
	
	@Component
	public static class NoGenericSimpleListener implements ISimpleListener {
		
		@Override
		public void invoke(Object msg) {
			log.info("NoGenericSimpleListener : {}", msg);
		}
	}
}
