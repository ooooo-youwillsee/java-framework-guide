package com.ooooo.core.request;

import com.ooooo.core.exception.APIException;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/22 10:32
 */
public interface ITemplate<R extends AbstractRequestEntity<?>, T extends AbstractResponseEntity<?>> {
	
	T execute(R request) throws APIException;
	
	TemplateProperties getTemplateProperties();
}
