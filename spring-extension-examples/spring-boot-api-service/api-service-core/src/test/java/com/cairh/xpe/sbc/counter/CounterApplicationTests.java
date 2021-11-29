package com.cairh.xpe.sbc.counter;

import com.ooooo.core.DefaultResult;
import com.ooooo.core.Invoker;
import com.ooooo.core.Result;
import com.ooooo.core.annotation.APIService;
import com.ooooo.core.annotation.APIServiceComponentScan;
import com.ooooo.core.annotation.IAPIService;
import com.ooooo.core.beans.AbstractServiceTypeBeanDefinitionProcessor;
import com.ooooo.core.beans.InvokerAdapterBeanDefinitionProcessor;
import com.ooooo.core.constants.ServiceType;
import com.ooooo.core.exception.APIException;
import com.ooooo.core.proxy.APIServiceConfig;
import com.ooooo.core.request.AbstractRequestEntity;
import com.ooooo.core.request.IRequestEntityProcessor;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Set;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author leizhijie
 * @since 2021/3/19 21:14
 */

@SpringBootApplication
@APIServiceComponentScan
public class CounterApplicationTests {
	
	@Bean
	public TestInvoker testInvoker() {
		return new TestInvoker();
	}
	
	@Bean
	public static InvokerAdapterBeanDefinitionProcessor testInvokerBeanDefinitionProcessorDecorator() {
		return new InvokerAdapterBeanDefinitionProcessor(TestInvoker.class, ServiceType.TEST_SERVICE);
	}
	
	@Bean
	public static AbstractServiceTypeBeanDefinitionProcessor testServiceTypeBeanDefinitionProcessor() {
		return new AbstractServiceTypeBeanDefinitionProcessor() {
			
			@Override
			protected Class<? extends Annotation> getAnnotation() {
				return TestService.class;
			}
			
			@Override
			protected Class<? extends IAPIService> getInterface() {
				return ITestService.class;
			}
			
			@Override
			protected Set<String> getPackageNames() {
				return Collections.singleton("com.cairh.xpe.sbc.counter.proxy");
			}
			
			@Override
			protected ServiceType getServiceType() {
				return ServiceType.TEST_SERVICE;
			}
		};
	}
	
	@Bean
	public IRequestEntityProcessor testIRequestEntityProcessor() {
		return new IRequestEntityProcessor() {
			@Override
			public AbstractRequestEntity<?> doProcoess(MethodInvocation invocation) {
				return new AbstractRequestEntity<Object>(invocation) {};
			}
			
			@Override
			public ServiceType getServiceType() {
				return ServiceType.TEST_SERVICE;
			}
		};
	}
	
	public interface ITestService extends IAPIService {
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
	@Documented
	@Inherited
	@APIService
	public @interface TestService {
	}
	
	
	public static class TestInvoker implements Invoker {
		
		@Override
		public Result invoke(MethodInvocation invocation, APIServiceConfig apiServiceConfig) throws APIException {
			Result result = new DefaultResult();
			result.setValue(invocation.getArguments()[0]);
			return result;
		}
	}
	
	
}
