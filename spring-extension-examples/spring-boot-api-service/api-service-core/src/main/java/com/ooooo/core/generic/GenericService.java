package com.ooooo.core.generic;

import com.ooooo.core.annotation.APIMapping;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

import static com.ooooo.core.constants.CounterConstants.GENERIC_SERVICE_URL_VALUE;

/**
 * 泛化接口标记类
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/8/24 14:49
 */
public interface GenericService {
	
	/**
	 * 判断该方法是否为泛化调用
	 *
	 * @param invocation
	 * @return
	 */
	static boolean isGeneric(MethodInvocation invocation) {
		Method method = invocation.getMethod();
		APIMapping methodMapping = AnnotationUtils.getAnnotation(method, APIMapping.class);
		if (methodMapping == null) {
			return false;
		}
		
		return GENERIC_SERVICE_URL_VALUE.equals(methodMapping.value());
	}
}
