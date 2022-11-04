package com.ooooo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.condition.JRE.JAVA_11;
import static org.junit.jupiter.api.condition.JRE.JAVA_8;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;

import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/10 16:54
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest
public class Junit5Test {
	
	@Autowired
	private UserService userService;
	
	@BeforeAll
	static void initAll() {
		log.info("initAll");
	}
	
	@BeforeEach
	void init() {
		log.info("init");
	}
	
	
	@Test
	void simpleTest() {
		String username = userService.findUsernameById(1L);
		assertEquals("username: 1", username);
	}
	
	
	@ParameterizedTest
	@MethodSource("p1")
	void methodSourceTest(Long id) {
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
	
	
	@RepeatedTest(10)
	void repeatedTest() {
		String username = userService.findUsernameById(1L);
		assertEquals("username: 1", username);
	}
	
	
	@Test
	@Disabled("Disabled until bug xxx has been resolved")
	void testWillBeSkipped() {
	}
	
	@Test
	@EnabledOnOs({LINUX, MAC})
	void onLinuxOrMac() {
	}
	
	@Test
	@EnabledForJreRange(min = JAVA_8, max = JAVA_11)
	void fromJava8to11() {
	}
	
	@Test
	@EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
	void onlyOn64BitArchitectures() {
	}
	
	@Test
	@EnabledIfEnvironmentVariable(named = "ENV", matches = "staging-server")
	void onlyOnStagingServer() {
	}
	
	@AfterEach
	void tearDown() {
		log.info("tearDown");
	}
	
	@AfterAll
	static void tearDownAll() {
		log.info("tearDownAll");
	}
}
