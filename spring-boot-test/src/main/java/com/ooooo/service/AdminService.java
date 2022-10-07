package com.ooooo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ooooo
 * @date 2021/09/11 10:47
 */
@Service
public class AdminService {
	
	@Autowired
	private UserService userService;
	
	public List<String> getUsernameList(List<Long> ids) {
		return ids.stream().map(id -> userService.findUsernameById(id)).collect(Collectors.toList());
	}
	
}
