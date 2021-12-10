package com.ooooo.service;

/**
 * @author ooooo
 * @date 2021/09/25 12:48
 * @since 1.0.0
 */
public interface UserService {
	
	String getUsernameById(Long id);
	
	String getUsernameByIdExceptionally(Long id);
}
