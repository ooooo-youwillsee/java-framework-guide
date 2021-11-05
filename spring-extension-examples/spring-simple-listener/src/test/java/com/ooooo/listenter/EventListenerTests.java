package com.ooooo.listenter;

import com.ooooo.SpringSimpleListenerApplicationTests;
import com.ooooo.annotation.SimpleMulticaster;
import com.ooooo.dao.entity.Message;
import com.ooooo.dao.entity.Order;
import com.ooooo.dao.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author leizhijie
 * @since 2021/2/24 15:10
 */
public class EventListenerTests extends SpringSimpleListenerApplicationTests {
	
	@Autowired
	private SimpleMulticaster multicaster;
	
	@Test
	public void user() {
		multicaster.multicast(new User("1", "tom"));
	}
	
	@Test
	public void userMessage() {
		multicaster.multicast(Message.payload(new User("1", "tom")));
	}
	
	@Test
	public void order() {
		multicaster.multicast(new Order("1", 12321));
	}
	
	@Test
	public void orderMessage() {
		multicaster.multicast(Message.payload(new Order("1", 12321)));
	}
}
