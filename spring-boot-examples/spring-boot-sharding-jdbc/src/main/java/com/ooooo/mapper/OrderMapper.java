package com.ooooo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooooo.entity.Order;
import org.apache.ibatis.annotations.Update;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/01 06:40
 * @since 1.0.0
 */
public interface OrderMapper extends BaseMapper<Order> {

	String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS t_order\n" +
					"(\n" +
					"    order_id   BIGINT AUTO_INCREMENT,\n" +
					"    user_id    INT    NOT NULL,\n" +
					"    address_id BIGINT NOT NULL,\n" +
					"    status     VARCHAR(50),\n" +
					"    PRIMARY KEY (order_id)\n" +
					")";

	String DROP_TABLE_USER = "TRUNCATE TABLE t_order";

	// ===================================


	@Update(CREATE_TABLE_USER)
	void createTable();

	@Update(DROP_TABLE_USER)
	void dropTable();


}