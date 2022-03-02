package com.ooooo.core.service;

import com.ooooo.core.request.AbstractRequestEntity;
import com.ooooo.core.request.AbstractResponseEntity;

/**
 * mocke request
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/8/26 09:54
 */
public interface MockService {
	
	AbstractResponseEntity<String> mock(AbstractRequestEntity<?> request);
	
	boolean hasMockData(AbstractRequestEntity<?> request);
}
