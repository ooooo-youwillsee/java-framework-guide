package com.ooooo.lambda;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author leizhijie
 * @since 2021/5/22 16:10
 */
public class LambdaUtilsTests {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LambdaUtilsTests.class);
	
	@Test
	public void resolveMethod() {
		User user = new User();
		
		Runnable getUsername = user::getUsername;
		Function<User, String> getUsername1 = User::getUsername;
		
		Function<String, String> getPassword = user::getPassword;
		BiFunction<User, String, String> getPassword1 = User::getPassword;
		
		String methodName = LambdaUtils.resolveMethod(User::getUsername);
		LOGGER.info("resovleMethod: {}", methodName);
	}
	
	
}
