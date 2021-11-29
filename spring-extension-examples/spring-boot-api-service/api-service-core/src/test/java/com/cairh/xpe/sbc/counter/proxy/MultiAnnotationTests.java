package com.cairh.xpe.sbc.counter.proxy;

import com.cairh.xpe.sbc.counter.CounterApplicationTests;
import com.cairh.xpe.sbc.counter.proxy.MultiAnnotationTests.MultiAnnotationBootstrap;
import com.ooooo.core.annotation.APIServiceComponentScan;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author leizhijie
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
	@APIServiceComponentScan("com.cairh.xpe.sbc.counter1")
	public @interface EnableTestService1 {
	}
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@Documented
	@APIServiceComponentScan("com.cairh.xpe.sbc.counter2")
	public @interface EnableTestService2 {
	}
	
	@EnableTestService1
	@EnableTestService2
	public static class MultiAnnotationBootstrap extends CounterApplicationTests {
	
	}
	
}
