package com.ooooo.autoconfigure;

import com.ooooo.core.beans.AdviceBeanDefinitionProcessor;
import com.ooooo.core.interceptor.DebugMethodInterceptor;
import com.ooooo.core.interceptor.DefaultParameterProcessMethodInterceptor;
import com.ooooo.core.interceptor.RequestEntityProcessMethodInterceptor;
import com.ooooo.core.interceptor.TraceServiceMethodInterceptor;
import com.ooooo.core.request.IRequestEntityProcessor;
import com.ooooo.core.service.TraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.ooooo.core.interceptor.DebugMethodInterceptor.DEBUG_METHOD_INTERCEPTOR_BEAN_NAME;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/12 15:59
 */
@Configuration
@Slf4j
public class APIServiceAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public AdviceBeanDefinitionProcessor advicesBeanDefinitionProcessor() {
		return new AdviceBeanDefinitionProcessor();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public RequestEntityProcessMethodInterceptor requestEntityProcessorInterceptor(@Autowired(required = false) List<IRequestEntityProcessor> processors) {
		return new RequestEntityProcessMethodInterceptor(processors);
	}
	
	@Bean
	@ConditionalOnMissingBean
	public DefaultParameterProcessMethodInterceptor defaultParameternterceptor() {
		return new DefaultParameterProcessMethodInterceptor();
	}
	
	@Bean(name = DEBUG_METHOD_INTERCEPTOR_BEAN_NAME)
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "cpe.service.interceptor.debug.enabled", havingValue = "true", matchIfMissing = true)
	public DebugMethodInterceptor debugMethodInterceptor() {
		log.info("开启[inteceptor.debug]功能");
		return new DebugMethodInterceptor();
	}
	
	@Bean
	@ConditionalOnBean(TraceService.class)
	@ConditionalOnMissingBean
	public TraceServiceMethodInterceptor traceMethodInterceptor(@Autowired(required = false) TraceService traceStore) {
		log.info("开启[inteceptor.trace]功能");
		return new TraceServiceMethodInterceptor(traceStore);
	}
	
}
