package com.ooooo.autoconfigure.env.dev;

import com.ooooo.autoconfigure.env.AbstractSimplePropertySource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.ooooo.constant.EnvironmentConstants.ENV_PREFIX;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2020/11/5 09:58 从请求参数中获取数据
 */
@Order(0)
public class RequestParamsPropertySource extends AbstractSimplePropertySource {
	
	public RequestParamsPropertySource() {
		super(ENV_PREFIX + "request_params");
	}
	
	@Override
	public Object getProperty(String name) {
		String propertyKey = ENV_PREFIX + name.replace(".", "_");
		String propertyValue = null;
		try {
			// 先请求参数中获取
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			if (attr == null) {
				return null;
			}
			HttpServletRequest request = attr.getRequest();
			if (request == null) {
				return null;
			}
			propertyValue = request.getParameter(propertyKey);
			if (StringUtils.isBlank(propertyValue)) {
				// 再从请求头中获取
				propertyValue = request.getHeader(propertyKey);
			}
		} catch (Throwable ignored) {
		}
		// 空字符也当做null
		propertyValue = defaultIfBlank(propertyValue, null);
		return propertyValue;
		
	}
	
}