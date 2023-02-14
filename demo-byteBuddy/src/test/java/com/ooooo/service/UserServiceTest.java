package com.ooooo.service;

import com.ooooo.dto.HelloRequest;
import com.ooooo.dto.HelloResponse;
import java.util.Arrays;
import java.util.concurrent.Callable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * see <a href="https://github.com/raphw/byte-buddy/blob/master/byte-buddy-dep/src/test/java/net/bytebuddy/ByteBuddyTutorialExamplesTest.java">example</a>s
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
class UserServiceTest {
	
	@SneakyThrows
	@Test
	void sayHello() {
		Class<? extends UserService> clazz = new ByteBuddy()
				.subclass(UserService.class)
				.method(ElementMatchers.not(ElementMatchers.isDeclaredBy(Object.class)))
				.intercept(MethodDelegation.to(LogInterceptor.class))
				.make()
				.load(ClassLoader.getSystemClassLoader())
				.getLoaded();
		
		UserService userService = clazz.getDeclaredConstructor().newInstance();
		HelloResponse helloResponse = userService.sayHello(new HelloRequest("1", "tom"));
		
		assertEquals("sayHello: 1", helloResponse.getId());
		assertEquals("sayHello: tom", helloResponse.getName());
	}
	
	
	public static class LogInterceptor {
		
		@RuntimeType
		public static Object invoke(@AllArguments @RuntimeType Object[] args, @SuperCall Callable<Object> call) {
			log.info("request: {}", Arrays.toString(args));
			Object result = null;
			try {
				result = call.call();
			} catch (Exception e) {
				log.info("exception: ", e);
			}
			log.info("response: {}", result);
			return result;
		}
	}
}