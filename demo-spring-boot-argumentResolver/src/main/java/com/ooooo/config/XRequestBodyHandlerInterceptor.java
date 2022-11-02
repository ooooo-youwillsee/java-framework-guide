package com.ooooo.config;

import com.ooooo.annotation.XRequestBody;
import com.ooooo.dao.entity.Req;
import java.util.Arrays;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/22 22:48
 */
public class XRequestBodyHandlerInterceptor implements HandlerInterceptor {

    public static final String XREQUEST_PARAM_PREFIX = "xrequest-";

    private final MappingJackson2HttpMessageConverter messageConverter;
	
	public XRequestBodyHandlerInterceptor(MappingJackson2HttpMessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			boolean hasXRequestBody = Arrays.stream(handlerMethod.getMethodParameters())
			                                .anyMatch(p -> p.hasParameterAnnotation(XRequestBody.class));
			if (hasXRequestBody) {
				HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
				Req req = (Req) messageConverter.read(Req.class, inputMessage);
				for (Entry<String, Object> entry : req.getBody().entrySet()) {
					request.setAttribute(XREQUEST_PARAM_PREFIX + entry.getKey(), entry.getValue());
				}
			}
		}
		return true;
	}
}
