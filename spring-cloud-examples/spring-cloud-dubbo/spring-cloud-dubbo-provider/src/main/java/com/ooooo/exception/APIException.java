package com.ooooo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/12/10 16:55
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class APIException extends RuntimeException {
	
	private String errorNo;
	
	private String errorInfo;
	
	public APIException(String errorNo, String errorInfo) {
		super(errorNo + ":" + errorInfo);
		this.errorNo = errorNo;
		this.errorInfo = errorInfo;
	}
}
