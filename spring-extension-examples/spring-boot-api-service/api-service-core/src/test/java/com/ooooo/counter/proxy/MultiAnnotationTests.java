package com.ooooo.counter.proxy;

import com.ooooo.core.annotation.APIServiceComponentScan;
import com.ooooo.counter.CounterApplicationTests;
import com.ooooo.counter.proxy.MultiAnnotationTests.MultiAnnotationBootstrap;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/7/5 17:38
 */
@SpringBootTest(classes = MultiAnnotationBootstrap.class)
public class MultiAnnotationTests {
	
	@Test
	public void testMultiAnnotation() {
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Documented
	@APIServiceComponentScan("com.ooooo.counter1")
	public @interface EnableTestService1 {
	}
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Documented
	@APIServiceComponentScan("com.ooooo.counter2")
	public @interface EnableTestService2 {
	}
	
	@EnableTestService1
	@EnableTestService2
	public static class MultiAnnotationBootstrap extends CounterApplicationTests {
	
	}
	
}
