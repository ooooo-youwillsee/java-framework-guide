package com.ooooo.core.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/22 10:44
 */
@Data
@NoArgsConstructor
public abstract class AbstractResponseEntity<T> {
	
	protected Error error = Error.SUCCESS;
	
	protected T data;
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Error {
		
		public static final Error SUCCESS = new Error("0", "ok");
		
		private String error_no;
		private String error_info;
	}
	
	
	public boolean isSuccess() {
		if (getError() == null) {
			return false;
		}
		return "0".equals(this.getError().getError_no());
	}
	
}
