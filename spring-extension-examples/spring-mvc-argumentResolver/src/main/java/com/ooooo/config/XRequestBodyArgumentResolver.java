package com.ooooo.config;

import com.alibaba.fastjson.JSON;
import com.ooooo.annotation.XRequestBody;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.ooooo.config.XRequestBodyHandlerInterceptor.XREQUEST_PARAM_PREFIX;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/22 21:57
 */
public class XRequestBodyArgumentResolver extends RequestResponseBodyMethodProcessor {

    private final ConversionService conversionService;

    public XRequestBodyArgumentResolver(List<HttpMessageConverter<?>> converters, ConversionService conversionService) {
		super(converters);
		this.conversionService = conversionService;
	}
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(XRequestBody.class);
	}
	
	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		String name = parameter.getParameterName();
		Class<?> parameterType = parameter.getParameterType();
		Object nativeRequest = webRequest.getNativeRequest();
		Object value = null;
		if (nativeRequest instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) nativeRequest;
			value = request.getAttribute(XREQUEST_PARAM_PREFIX + name);
			// simple use json
			value = JSON.parseObject(JSON.toJSONString(value), parameterType);
			// you also can use conversionService
			//if (conversionService.canConvert(value.getClass(), parameterType)) {
			//	value = conversionService.convert(value, parameterType);
			//}
		}
		return value;
	}
}
