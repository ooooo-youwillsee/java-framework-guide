package com.cairh.xpe.sbc.counter.proxy;

import com.cairh.xpe.sbc.counter.CounterApplicationTests;
import com.cairh.xpe.sbc.counter.CounterApplicationTests.ITestService;
import com.cairh.xpe.sbc.counter.CounterApplicationTests.TestService;
import com.ooooo.core.annotation.APIMapping;
import com.ooooo.core.annotation.APIService;
import com.ooooo.core.annotation.IAPIService;
import com.ooooo.core.constants.ServiceType;
import com.ooooo.core.proxy.APIServiceConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

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
		Assertions.assertEquals(bean.sayHello("123"), "123");
		IAPIService service = bean;
		APIServiceConfig apiServiceProxyFactoryBean = service.getAPIServiceConfig();
		Assertions.assertEquals(apiServiceProxyFactoryBean.getServiceType(), ServiceType.TEST_SERVICE);
	}
	
	
	@Test
	public void testApiServiceWithAnnotation() {
		HelloService2 bean = context.getBean(HelloService2.class);
		Assertions.assertEquals(bean.sayHello("123"), "123");
		IAPIService service = (IAPIService) bean;
		APIServiceConfig proxBean = service.getAPIServiceConfig();
		Assertions.assertEquals(proxBean.getServiceType(), ServiceType.TEST_SERVICE);
	}
	
	@Test
	public void testApiServiceWithPackageName() {
		HelloService3 bean = context.getBean(HelloService3.class);
		Assertions.assertEquals(bean.sayHello("123"), "123");
		IAPIService service = (IAPIService) bean;
		APIServiceConfig proxBean = service.getAPIServiceConfig();
		Assertions.assertEquals(proxBean.getServiceType(), ServiceType.TEST_SERVICE);
	}
}


@APIService
interface HelloService1 extends ITestService {
	
	@APIMapping("HelloService1#sayHello1")
	String sayHello(String name);
	
}

@TestService
interface HelloService2 {
	
	String sayHello(String name);
	
}

@APIService
interface HelloService3 {
	
	String sayHello(String name);
	
}


