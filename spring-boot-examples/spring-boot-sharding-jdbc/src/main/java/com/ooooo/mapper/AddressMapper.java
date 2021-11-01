package com.ooooo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooooo.entity.Address;
import org.apache.ibatis.annotations.Update;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/01 06:40
 * @since 1.0.0
 */
public interface AddressMapper extends BaseMapper<Address> {

	String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS t_address\n" +
					"(\n" +
					"    address_id   BIGINT       NOT NULL,\n" +
					"    address_name VARCHAR(100) NOT NULL,\n" +
					"    PRIMARY KEY (address_id)\n" +
					");";

	String DROP_TABLE_USER = "TRUNCATE TABLE t_address";

	// ===================================


	@Update(CREATE_TABLE_USER)
	void createTable();

	@Update(DROP_TABLE_USER)
	void dropTable();


}
