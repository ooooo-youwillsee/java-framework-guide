package com.ooooo.service;

import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

/**
 * @author ooooo
 * @date 2021/09/11 10:50
 */
@SpringBootTest
class SpyBeanTest {
	
	@Autowired
	private AdminService adminService;
	
	@SpyBean
	private UserService userService;
	
	@Test
	void getUsernameList() {
		doReturn("11111").when(userService).findUsernameById(1L);
		
		List<String> usernameList = adminService.getUsernameList(Lists.newArrayList(1L, 2L));
		assertEquals("11111", usernameList.get(0));
		assertEquals("username: 2", usernameList.get(1));
	}
	
}