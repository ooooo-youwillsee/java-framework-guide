package com.cairh.cpe.counter.generic;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.not;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * <a href="https://github.com/raphw/byte-buddy/issues/1428">github issue</a>
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class DemoServiceTest {
	
	@Test
	public void test() {
		Class<?> clazz = new ByteBuddy()
				.subclass(A.class)
				.implement(B.class)
				.intercept(FixedValue.value(new C()))
				.method(not(isDeclaredBy(Object.class)))
				.intercept(MethodDelegation.to(new AInterceptor()))
				.make()
				.load(Thread.currentThread().getContextClassLoader())
				.getLoaded();
		
		assertTrue(B.class.isAssignableFrom(clazz));
	}

	public static class A {

		public String sayHello() {
			return "Hello";
		}
	}

	public interface B {

		String getA();
	}

	public static class C {

	}

	public static class AInterceptor {

		@RuntimeType
		public Object invoke(@RuntimeType @AllArguments Object[] args,
												 @Origin Method method,
												 @SuperCall Callable<Object> callable) throws Exception {

			return callable.call();
		}
	}
}
