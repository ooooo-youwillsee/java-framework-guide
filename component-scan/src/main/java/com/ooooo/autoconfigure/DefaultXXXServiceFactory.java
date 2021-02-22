package com.ooooo.autoconfigure;

import com.ooooo.annotation.XXXRequestMapping;
import com.ooooo.factory.ServiceFactory;
import com.ooooo.properties.ServiceProperties;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author leizhijie
 * @since 2021/2/22 15:07 simple use jdk proxy
 */
public class DefaultXXXServiceFactory implements ServiceFactory, InvocationHandler {
	
	private final ConcurrentHashMap<Class<?>, Object> cache;
	private final ServiceProperties serviceProperties;
	private final RestTemplate template;
	
	public DefaultXXXServiceFactory(ServiceProperties serviceProperties, RestTemplate template) {
		cache = new ConcurrentHashMap<>(32);
		this.serviceProperties = serviceProperties;
		this.template = template;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getService(Class<T> interfaceClazz) {
		return (T) cache.computeIfAbsent(interfaceClazz, __ -> createProxy(interfaceClazz));
	}
	
	private Object createProxy(Class<?> interfaceClazz) {
		return Proxy.newProxyInstance(interfaceClazz.getClassLoader(), new Class[]{interfaceClazz}, this);
	}
	
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		URI uri = buildURI(resolveUrl(method));
		HttpMethod httpMethod = resolveMethod(method);
		HttpEntity<?> httpEntity = convertHttpEntity(args);
		Class<?> responseType = resolveResponseType(method);
		ResponseEntity<?> responseEntity = template.exchange(uri, httpMethod, httpEntity, responseType);
		handleException(responseEntity);
		return responseEntity.getBody();
	}
	
	
	private URI buildURI(String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance()
		                                                   .scheme("http")
		                                                   .host(serviceProperties.getIp())
		                                                   .port(serviceProperties.getPort())
		                                                   .path(serviceProperties.getPrefix())
		                                                   .path(url);
		
		return builder.build().encode(StandardCharsets.UTF_8).toUri();
	}
	
	private String resolveUrl(Method method) {
		XXXRequestMapping annotation = AnnotationUtils.getAnnotation(method, XXXRequestMapping.class);
		if (annotation == null) {
			throw new IllegalArgumentException("'@XXXRequestMapping' anonation isn't present ");
		}
		return annotation.value();
	}
	
	private HttpMethod resolveMethod(Method method) {
		XXXRequestMapping annotation = AnnotationUtils.getAnnotation(method, XXXRequestMapping.class);
		if (annotation == null) {
			throw new IllegalArgumentException("'@XXXRequestMapping' anonation isn't present ");
		}
		return annotation.method();
	}
	
	private HttpEntity<?> convertHttpEntity(Object[] args) {
		if (args == null || args.length == 0) {
			return new HttpEntity<>(null);
		}
		if (args.length > 1) {
			// only post json
			throw new IllegalArgumentException("request parameter length only one");
		}
		return new HttpEntity<>(args[0]);
	}
	
	private Class<?> resolveResponseType(Method method) {
		// simple type
		return method.getReturnType();
	}
	
	private void handleException(ResponseEntity<?> responseEntity) {
		if (responseEntity.getStatusCode() != HttpStatus.OK) {
			throw new IllegalArgumentException("request fail");
		}
	}
}
