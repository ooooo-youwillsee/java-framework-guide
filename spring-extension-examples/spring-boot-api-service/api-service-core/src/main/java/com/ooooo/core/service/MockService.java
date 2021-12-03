package com.ooooo.core.service;

import com.ooooo.core.request.AbstractRequestEntity;
import com.ooooo.core.request.AbstractResponseEntity;

/**
 * 对请求进行mock
 *
 * @author leizhijie
 * @since 2021/8/26 09:54
 */
public interface MockService {
	
	AbstractResponseEntity<String> mock(AbstractRequestEntity<?> request);
	
	boolean hasMockData(AbstractRequestEntity<?> request);
}