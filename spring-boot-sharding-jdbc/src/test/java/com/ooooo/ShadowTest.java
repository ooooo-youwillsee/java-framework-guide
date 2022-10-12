package com.ooooo;

import com.ooooo.dao.entity.ShadowUser;
import com.ooooo.dao.mapper.ShadowUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.shadow.api.shadow.column.ColumnShadowAlgorithm;
import org.apache.shardingsphere.shadow.api.shadow.column.PreciseColumnShadowValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/02 06:04
 * @see ColumnShadowAlgorithm#isShadow(PreciseColumnShadowValue)
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest
@ActiveProfiles("shadow")
public class ShadowTest {


	@Autowired
	private ShadowUserMapper shadowUserMapper;

	private ShadowUser user;

	@BeforeEach
	public void beforeEach() {
		log.info("需要提前创建表");
	}


	@Test
	public void insert() {
		user = new ShadowUser();
		user.setUserName("jack");
		user.setPwd("123456");
		user.setUserType("1");

		shadowUserMapper.insert(user);
	}

	@Test
	public void update() {
		insert();

		ShadowUser updateUser = new ShadowUser();
		updateUser.setUserName("tom");
		updateUser.setUserId(user.getUserId());
		updateUser.setUserType(user.getUserType());

		shadowUserMapper.updateById(updateUser);
	}


	@Test
	public void delete() {
		insert();

		ShadowUser deleteUser = new ShadowUser();
		deleteUser.setUserId(user.getUserId());
		deleteUser.setUserType(user.getUserType());

		shadowUserMapper.deleteById(deleteUser);
	}

	@Test
	public void select() {
		insert();

		shadowUserMapper.selectList(null);
	}


}
