package com.ooooo.annotation;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.handler.AbstractHandlerMethodMapping;

/**
 * @author leizhijie
 * @since 2021/2/24 15:31
 * <p>
 * HandlerMapping
 */
public class XRequestMappingHandlerMapping extends AbstractHandlerMethodMapping<XRequestMappingMethodInfo> implements Ordered {
	
	
	@Override
	protected boolean isHandler(Class<?> beanType) {
		return AnnotatedElementUtils.hasAnnotation(beanType, XRequestMapping.class);
	}
	
	@Override
	protected XRequestMappingMethodInfo getMappingForMethod(Method method, Class<?> handlerType) {
		AnnotationUtils.getAnnotation(method, XRequestMapping.class);
		XRequestMapping classXRequestMapping = AnnotatedElementUtils.findMergedAnnotation(handlerType, XRequestMapping.class);
		XRequestMapping methodXRequestMapping = AnnotatedElementUtils.findMergedAnnotation(method, XRequestMapping.class);
		String path = classXRequestMapping.value() + methodXRequestMapping.value();
		HttpMethod[] httpMethod = methodXRequestMapping.method();
		if (ArrayUtils.isEmpty(httpMethod)) {
			httpMethod = classXRequestMapping.method();
		}
		return new XRequestMappingMethodInfo(path, httpMethod);
	}
	
	@Override
	protected Set<String> getMappingPathPatterns(XRequestMappingMethodInfo mapping) {
		HashSet<String> res = new HashSet<>();
		res.add(mapping.getPath());
		return res;
	}
	
	@Override
	protected XRequestMappingMethodInfo getMatchingMapping(XRequestMappingMethodInfo mapping, HttpServletRequest request) {
		boolean pass = ArrayUtils.isEmpty(mapping.httpMethod) || ArrayUtils.contains(mapping.httpMethod, HttpMethod.valueOf(request.getMethod()));
		if (!pass) {
			return null;
		}
		pass = request.getRequestURI().equals(mapping.getPath());
		if (pass) {
			return mapping;
		}
		return null;
	}
	
	@Override
	protected Comparator<XRequestMappingMethodInfo> getMappingComparator(HttpServletRequest request) {
		return (o1, o2) -> 0;
	}
	
	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE - 1;
	}
}
