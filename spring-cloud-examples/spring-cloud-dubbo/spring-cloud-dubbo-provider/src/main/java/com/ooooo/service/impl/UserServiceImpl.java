package com.ooooo.service.impl;

import com.ooooo.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author ooooo
 * @date 2021/09/25 13:00
 * @since 1.0.0
 */
@DubboService
public class UserServiceImpl implements UserService {

	@Override
	public String getUsernameById(Long id) {
		return "username" + id;
	}

}
