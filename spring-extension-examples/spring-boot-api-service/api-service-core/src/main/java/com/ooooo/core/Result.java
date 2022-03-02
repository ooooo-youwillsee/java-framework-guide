package com.ooooo.core;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/12 16:07
 */
public interface Result {
	
	/**
	 * Get invoke result.
	 *
	 * @return result. if no result return null.
	 */
	Object getValue();
	
	void setValue(Object value);
	
	/**
	 * Get exception.
	 *
	 * @return exception. if no exception return null.
	 */
	Throwable getException();
	
	void setException(Throwable exception);
}
