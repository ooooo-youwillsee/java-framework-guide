package com.ooooo.core;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/20 09:20
 */
public class DefaultResult implements Result {
	
	@Getter
	@Setter
	private Object value;
	
	@Getter
	@Setter
	private Throwable exception;
}
