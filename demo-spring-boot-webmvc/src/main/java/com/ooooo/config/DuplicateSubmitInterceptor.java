package com.ooooo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

import static java.lang.String.valueOf;

/**
 * check whether the request is duplicated in interval time
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/22 16:10
 * @since 1.0.0
 */
@Slf4j
@Component
public class DuplicateSubmitInterceptor implements HandlerInterceptor {
	
	/**
	 * must configure redisTemplate
	 */
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	private static final String REDIS_LOCK_PREFIX = "idempotency.submit.config:%s:%s:%s";
	
	/**
	 * default lock time
	 */
	private static final int DEFAULT_LOCK_TIME = 3;
	
	/**
	 * whether determine the request is same as the before
	 */
	public static final String PARAM_CHECK_REQUEST_MD5 = "CHECK_REQUEST_MD5";
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod method) {
			if (method.getBeanType().isAnnotationPresent(Controller.class) || method.getBeanType().isAnnotationPresent(RestController.class)) {
				
				String requestURI = requestURI(request);
				String md5 = request.getParameter(PARAM_CHECK_REQUEST_MD5);
				// retrieve userId  from session or Authorization
				String userId = retrieveUserId(request);
				// build redis key
				String redisKey = buildKey(userId, requestURI, md5);
				
				boolean exist = redisTemplate.hasKey(redisKey);
				if (exist) {
					throw new RuntimeException("don't resubmit the request");
				}
				redisTemplate.boundValueOps(redisKey).set(valueOf(System.currentTimeMillis()), DEFAULT_LOCK_TIME, TimeUnit.SECONDS);
			}
		}
		return true;
	}
	
	private String retrieveUserId(HttpServletRequest request) {
		return "0";
	}
	
	public String buildKey(String userId, String url, String md5) {
		return String.format(REDIS_LOCK_PREFIX, userId, url, md5);
	}
	
	
	private String requestURI(HttpServletRequest request) {
		return request.getRequestURI();
	}
	
}
