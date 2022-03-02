package com.ooooo.core.constants;

import com.ooooo.core.beans.AbstractServiceTypeBeanDefinitionProcessor;
import com.ooooo.core.beans.AdviceBeanDefinitionProcessor;
import com.ooooo.core.beans.InvokerAdapterBeanDefinitionProcessor;
import com.ooooo.core.proxy.APIServiceConfig;
import lombok.Getter;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/20 09:05
 */
public enum BeanDefinitionProcessorType {
	
	
	/**
	 * 必须先识别serviceType
	 *
	 * @see ServiceType
	 * @see AbstractServiceTypeBeanDefinitionProcessor
	 */
	SERVIC_TYPE,
	/**
	 * 对serviceType插入相应的Invoker实现类
	 *
	 * @see InvokerAdapterBeanDefinitionProcessor
	 */
	INVOKER_ADAPTER,
	/**
	 * @see APIServiceConfig
	 * @see AdviceBeanDefinitionProcessor
	 */
	ADVICE,
	
	;
	
	@Getter
	private final int order;
	
	
	BeanDefinitionProcessorType() {
		this.order = this.ordinal() * 100;
	}
	
}
