package com.ooooo.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooooo.dao.entity.OrderItem;
import org.apache.ibatis.annotations.Update;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/01 06:40
 * @since 1.0.0
 */
public interface OrderItemMapper extends BaseMapper<OrderItem> {

	String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS t_order_item\n" +
					"(\n" +
					"    order_item_id BIGINT AUTO_INCREMENT,\n" +
					"    order_id      BIGINT,\n" +
					"    user_id       INT NOT NULL,\n" +
					"    status        VARCHAR(50),\n" +
					"    PRIMARY KEY (order_item_id)\n" +
					")";

	String TRUNCATE_TABLE_USER = "TRUNCATE TABLE t_order_item";

	// ===================================


	@Update(CREATE_TABLE_USER)
	void createTable();

	@Update(TRUNCATE_TABLE_USER)
	void truncateTable();


}
