package com.ooooo.controller;

import com.ooooo.dao.entity.Req;
import com.ooooo.dao.entity.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leizhijie
 * @since 2021/2/22 15:25
 */
@Slf4j
@RestController
public class TestController {
	
	@PostMapping("/test")
	public Resp test(@RequestBody Req req) {
		String id = req.getId();
		log.info("id : {}", id);
		return new Resp(id, "tom", 18);
	}
}
