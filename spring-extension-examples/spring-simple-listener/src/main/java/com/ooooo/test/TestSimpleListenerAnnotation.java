package com.ooooo.test;

import com.ooooo.annotation.SimpleListener;
import com.ooooo.dao.entity.Message;
import com.ooooo.dao.entity.Order;
import com.ooooo.dao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/23 17:22
 */
@Configuration
@Slf4j
public class TestSimpleListenerAnnotation {
	
	@SimpleListener
	public void recevieOrderMessage(Message<Order> msg) {
		log.info("recevieOrderMessage : {}", msg);
	}
	
	@SimpleListener
	public void recevieUserMessage(Message<User> msg) {
		log.info("recevieUserMessage : {}", msg);
	}
	
	@SimpleListener
	public void recevieNotGenericMessage(Message msg) {
		log.info("recevieNotGenericMessage : {}", msg);
	}
	
	@SimpleListener
	public void recevieUser(User msg) {
		log.info("recevieUser : {}", msg);
		
	}
	
	@SimpleListener
	public void recevieOrder(Order msg) {
		log.info("recevieOrder : {}", msg);
		
	}
}
