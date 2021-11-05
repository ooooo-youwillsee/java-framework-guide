package com.ooooo.controller;

import com.alibaba.fastjson.JSON;
import com.ooooo.annotation.XRequestBody;
import com.ooooo.dao.entity.Resp;
import com.ooooo.dao.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leizhijie
 * @since 2021/2/22 21:53
 */
@Slf4j
@RestController
public class UserController {
	
	@PostMapping("/user")
	public Resp user(@XRequestBody User user) {
		log.info("receive user: {}", JSON.toJSONString(user));
		Resp resp = new Resp();
		BeanUtils.copyProperties(user, resp);
		return resp;
	}
}
