package com.ooooo.annotation;

import org.springframework.beans.factory.Aware;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/23 18:42
 */
public interface SimpleMulticasterAware extends Aware {
	
	void setSimpleMulticaster(SimpleMulticaster simpleMulticaster);
	
}
