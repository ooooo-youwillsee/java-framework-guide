package com.ooooo;

import com.ooooo.dao.entity.Address;
import com.ooooo.dao.entity.Order;
import com.ooooo.dao.entity.OrderItem;
import com.ooooo.dao.entity.User;
import com.ooooo.dao.mapper.AddressMapper;
import com.ooooo.dao.mapper.OrderItemMapper;
import com.ooooo.dao.mapper.OrderMapper;
import com.ooooo.dao.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/02 06:04
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("readwrite-splitting")
public class ReadWriteSplittingTest {

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	@Autowired
	private UserMapper userMapper;

	private Order order;

	private OrderItem orderItem;

	@BeforeEach
	public void beforeEach() {
		log.info("需要提前创建表, 搭建主从");
	}


	@Test
	public void insert() {
		order = new Order();
		order.setStatus("1");
		order.setAddressId(1L);
		order.setUserId(1L);
		orderMapper.insert(order);

		log.info("order id: {}", order.getOrderId());

		orderItem = new OrderItem();
		orderItem.setOrderId(order.getOrderId());
		orderItem.setStatus("1");
		orderItem.setUserId(1L);
		orderItemMapper.insert(orderItem);

		log.info("orderItem id: {}", orderItem.getOrderItemId());
	}

	@Test
	public void update() {
		insert();

		Order updateOrder = new Order();
		updateOrder.setOrderId(order.getOrderId());
		updateOrder.setStatus("2");
		orderMapper.updateById(updateOrder);

		OrderItem updateOrderItem = new OrderItem();
		updateOrderItem.setOrderItemId(orderItem.getOrderItemId());
		updateOrderItem.setStatus("2");
		orderItemMapper.updateById(updateOrderItem);
	}


	@Test
	public void delete() {
		insert();

		orderMapper.deleteById(order.getOrderId());
		orderItemMapper.deleteById(orderItem.getOrderItemId());
	}

	@Test
	public void select() {
		insert();

		orderMapper.selectList(null);
		orderItemMapper.selectList(null);
	}


	@Test
	public void insertUser() {
		User user = new User();
		user.setUserName("jack");
		user.setPwd("123456");

		userMapper.insert(user);
	}


	@Test
	public void selectAddress() {
		Address address = new Address();
		address.setAddressName("xxx");

		addressMapper.insert(address);

		addressMapper.selectList(null);
	}
}
