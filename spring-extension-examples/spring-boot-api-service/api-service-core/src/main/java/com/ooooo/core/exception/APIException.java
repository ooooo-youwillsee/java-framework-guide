package com.ooooo.core.exception;

import lombok.Getter;

/**
 * @author leizhijie
 * @since 2021/3/18 16:33
 */
public class APIException extends RuntimeException {
	
	@Getter
	protected String error_no = "0";
	
	@Getter
	protected String error_info;
	
	@Getter
	protected String error_path_info;
	
	@Getter
	private final String message;
	
	public APIException(String message) {
		super(message);
		this.message = message;
	}
	
	public APIException(String error_no, String error_info, String error_path_info) {
		this(String.format("%s:%s:%s", error_no, error_info, error_path_info));
		this.error_no = error_no;
		this.error_info = error_info;
		this.error_path_info = error_path_info;
	}
	
	public boolean isSuccess() {
		return "0".equals(error_no);
	}
}
