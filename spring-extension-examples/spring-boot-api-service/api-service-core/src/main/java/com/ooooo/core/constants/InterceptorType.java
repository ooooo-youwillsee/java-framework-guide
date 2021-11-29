package com.ooooo.core.constants;

import com.ooooo.core.Invoker;
import com.ooooo.core.beans.InvokerAdapterBeanDefinitionProcessor;
import com.ooooo.core.interceptor.DebugMethodInterceptor;
import com.ooooo.core.interceptor.DefaultParameterProcessMethodInterceptor;
import com.ooooo.core.interceptor.InvokerMethodInterceptor;
import com.ooooo.core.interceptor.RequestEntityProcessMethodInterceptor;
import com.ooooo.core.interceptor.TraceServiceMethodInterceptor;
import com.ooooo.core.request.IRequestEntityProcessor;
import lombok.Getter;

/**
 * @author leizhijie
 * @since 2021/3/19 20:05
 */
public enum InterceptorType {
	
	EXTENSION, // 扩展,预留
	/**
	 * 把invocation转换为ResponseEntity
	 *
	 * @see IRequestEntityProcessor
	 * @see RequestEntityProcessMethodInterceptor
	 */
	REQUEST_ENTITY_PROCESSOR,
	/**
	 * 填充默认参数
	 *
	 * @see DefaultParameterProcessMethodInterceptor
	 */
	FILL_DEFAULT_PARAMETER,
	/**
	 * 用于调试，日志
	 *
	 * @see DebugMethodInterceptor
	 */
	DEBUG,
	/**
	 * 用于trace， 重发请求
	 *
	 * @see TraceServiceMethodInterceptor
	 */
	TRACE,
	REQUEST_PROCESS, // 请求,预留
	/**
	 * 执行
	 *
	 * @see InvokerAdapterBeanDefinitionProcessor
	 * @see InvokerMethodInterceptor
	 * @see Invoker
	 */
	INVOKER,
	RESPONSE_PROCESS, // 响应，预留
	
	;
	
	@Getter
	int order;
	
	InterceptorType() {
		this.order = this.ordinal() * 100;
	}
	
}
