package com.ooooo.core.beans;

import com.ooooo.core.constants.BeanDefinitionProcessorType;
import com.ooooo.core.interceptor.APIServiceConfigMethodInterceptor;
import com.ooooo.core.interceptor.DebugMethodInterceptor;
import com.ooooo.core.proxy.APIServiceConfig;
import lombok.Setter;
import org.aopalliance.aop.Advice;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.Collection;
import java.util.List;

import static com.ooooo.core.interceptor.DebugMethodInterceptor.DEBUG_METHOD_INTERCEPTOR_BEAN_NAME;

/**
 * 最后的处理,添加advice
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/20 21:54
 */
public class AdviceBeanDefinitionProcessor implements BeanDefinitionProcessor, Ordered, ApplicationContextAware {
	
	@Setter
	private ApplicationContext applicationContext;
	
	@Override
	public void handle(BeanDefinition holder, APIServiceConfig config) {
		List<Advice> advices = config.getTmpAdvices();
		addAPIServiceMethodInterceptor(advices, APIServiceConfigMethodInterceptor.class);
		addDefaultMethodInterceptor(advices);
		AnnotationAwareOrderComparator.sort(advices);
	}
	
	private void addDefaultMethodInterceptor(List<Advice> advices) {
		if (applicationContext.containsBean(DEBUG_METHOD_INTERCEPTOR_BEAN_NAME)) {
			DebugMethodInterceptor debugMethodInterceptor = (DebugMethodInterceptor) applicationContext.getBean(DEBUG_METHOD_INTERCEPTOR_BEAN_NAME);
			advices.add(debugMethodInterceptor);
		}
	}
	
	private void addAPIServiceMethodInterceptor(List<Advice> advices, Class<? extends Advice> adviceClass) {
		Collection<? extends Advice> collection = applicationContext.getBeansOfType(adviceClass).values();
		advices.addAll(collection);
	}
	
	@Override
	public int getOrder() {
		return BeanDefinitionProcessorType.ADVICE.getOrder();
	}
	
}
