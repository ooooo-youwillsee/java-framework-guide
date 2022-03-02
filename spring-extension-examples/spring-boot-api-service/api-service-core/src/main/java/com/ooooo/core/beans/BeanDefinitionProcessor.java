package com.ooooo.core.beans;

import com.ooooo.core.proxy.APIServiceConfig;
import org.springframework.beans.factory.config.BeanDefinition;

/**
 * 对beanDefinition处理
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/18 20:51
 */
public interface BeanDefinitionProcessor {
	
	void handle(BeanDefinition holder, APIServiceConfig config);
}
