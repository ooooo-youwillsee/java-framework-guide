package com.ooooo.service;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/10 16:54
 * @since 1.0.0
 */
@SpringBootTest
public class Junit5Test {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void simpleTest() {
		String username = userService.findUsernameById(1L);
		
		assertEquals("username: 1", username);
	}
	
	
	@ParameterizedTest
	@MethodSource("p1")
	public void methodSourceTest(Long id) {
		String username = userService.findUsernameById(id);
		
		assertThat(username.contains(id.toString()));
	}
	
	static Stream<Long> p1() {
		return Stream.of(1L, 2L, 3L);
	}
	
	@ParameterizedTest
	@MethodSource("p2")
	public void methodSourceTest2(Long id, String username) {
		String nickname = userService.findNicknameByIdAndUsername(id, username);
		
		assertThat(nickname.contains(id.toString() + username));
	}
	
	static Stream<Object[]> p2() {
		return Stream.of(new Object[]{1L, "tom"}, new Object[]{2L, "jerry"});
	}
}
