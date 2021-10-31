package com.ooooo;

import com.ooooo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/10/31 08:55
 * @since 1.0.0
 */
@SpringBootTest
class DubboConsumerApplicationTest {

	@MockBean
	private UserService userService;

	@Test
	public void testDubboReferenceMockBean() {
		when(userService.getUsernameById(any())).thenReturn("mock username");

		String username1 = userService.getUsernameById(1L);
		assertEquals("mock username", username1);

		String username2 = userService.getUsernameById(1L);
		assertEquals("mock username", username2);
	}
}