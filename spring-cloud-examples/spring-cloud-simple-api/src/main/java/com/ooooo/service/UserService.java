package com.ooooo.service;

import org.springframework.stereotype.Service;

/**
 * @author ooooo
 * @date 2021/09/11 10:44
 */
@Service
public class UserService {

	public String findUsernameById(Long id) {
		return "username: " + id;
	}
}
