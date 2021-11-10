package com.ooooo.service;

import java.util.List;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;

/**
 * @author ooooo
 * @date 2021/09/11 10:50
 */
@SpringBootTest
class MockBeanTest {

	@Autowired
	private AdminService adminService;

	@MockBean
	private UserService userService;

	@Mock
	private List<String> nums;

	@Test
	void getUsernameList() {
		given(userService.findUsernameById(1L)).willReturn("11111");

		List<String> usernameList = adminService.getUsernameList(Lists.newArrayList(1L, 2L));
		assertEquals("11111", usernameList.get(0));
		assertNull(usernameList.get(1));
	}


	@Test
	void testNums() {
		given(nums.get(0)).willReturn("11");

		nums.add("1");
		assertEquals("11", nums.get(0));
	}
}