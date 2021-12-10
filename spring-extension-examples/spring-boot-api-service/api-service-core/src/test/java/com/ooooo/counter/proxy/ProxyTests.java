package com.ooooo.counter.proxy;

import com.ooooo.core.annotation.APIMapping;
import com.ooooo.core.annotation.APIService;
import com.ooooo.core.annotation.IAPIService;
import com.ooooo.core.proxy.APIServiceConfig;
import com.ooooo.counter.CounterApplicationTests;
import com.ooooo.counter.CounterApplicationTests.ITestService;
import com.ooooo.counter.CounterApplicationTests.TestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static com.ooooo.core.constants.ServiceType.TEST_SERVICE;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author leizhijie
 * @since 2021/3/19 21:16
 */
@SpringBootTest(classes = CounterApplicationTests.class)
public class ProxyTests {
	
	@Autowired
	private ApplicationContext context;
	
	@Test
	public void testApiServiceWithInterface() {
		HelloService1 bean = context.getBean(HelloService1.class);
		assertEquals(bean.sayHello("123"), "123");
		IAPIService service = bean;
		APIServiceConfig apiServiceProxyFactoryBean = service.getAPIServiceConfig();
		assertEquals(apiServiceProxyFactoryBean.getServiceType(), TEST_SERVICE);
	}
	
	
	@Test
	public void testApiServiceWithAnnotation() {
		HelloService2 bean = context.getBean(HelloService2.class);
		assertEquals(bean.sayHello("123"), "123");
		IAPIService service = (IAPIService) bean;
		APIServiceConfig proxBean = service.getAPIServiceConfig();
		assertEquals(proxBean.getServiceType(), TEST_SERVICE);
	}
	
	@Test
	public void testApiServiceWithPackageName() {
		HelloService3 bean = context.getBean(HelloService3.class);
		assertEquals(bean.sayHello("123"), "123");
		IAPIService service = (IAPIService) bean;
		APIServiceConfig proxBean = service.getAPIServiceConfig();
		assertEquals(proxBean.getServiceType(), TEST_SERVICE);
	}
}


@APIService
interface HelloService1 extends ITestService {
	
	@APIMapping("HelloService1#sayHello")
	String sayHello(String name);
	
}

@TestService
interface HelloService2 {
	
	@APIMapping("HelloService2#sayHello")
	String sayHello(String name);
	
}

@APIService
interface HelloService3 {
	
	@APIMapping("HelloService3#sayHello")
	String sayHello(String name);
	
}


