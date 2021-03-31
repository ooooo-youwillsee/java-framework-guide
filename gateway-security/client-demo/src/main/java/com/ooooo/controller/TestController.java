package com.ooooo.controller;

import com.ooooo.service.AccessTokenResponse;
import com.ooooo.service.OAuth2Service;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leizhijie
 * @since 2021/3/31 16:30
 */
@RestController
public class TestController {
	
	@Autowired
	private OAuth2Service oAuth2Service;
	
	
	@GetMapping(value = "/code", produces = MediaType.APPLICATION_JSON_VALUE)
	public AccessTokenResponse code(HttpServletRequest request) {
		return oAuth2Service.create(request);
	}
	
	@GetMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
	public AccessTokenResponse refresh(HttpServletRequest request) {
		return oAuth2Service.refresh(request);
	}
	
	@GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public String test(HttpServletRequest request) {
		return oAuth2Service.testApi(oAuth2Service.create(request));
	}
	
}