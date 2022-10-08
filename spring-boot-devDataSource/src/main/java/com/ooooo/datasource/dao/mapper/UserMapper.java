package com.ooooo.datasource.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ooooo.datasource.dao.entity.User;
import org.apache.ibatis.annotations.Update;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/01 06:40
 * @since 1.0.0
 */
public interface UserMapper extends BaseMapper<User> {
	
	String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS t_user\n" +
			"(\n" +
			"    user_id   BIGINT NOT NULL AUTO_INCREMENT,\n" +
			"    user_name VARCHAR(200),\n" +
			"    pwd       VARCHAR(200),\n" +
			"    PRIMARY KEY (user_id)\n" +
			")";
	
	String TRUNCATE_TABLE_USER = "TRUNCATE TABLE t_order";
	
	// ===================================
	
	
	@Update(CREATE_TABLE_USER)
	void createTable();
	
	@Update(TRUNCATE_TABLE_USER)
	void truncateTable();
	
	
}
