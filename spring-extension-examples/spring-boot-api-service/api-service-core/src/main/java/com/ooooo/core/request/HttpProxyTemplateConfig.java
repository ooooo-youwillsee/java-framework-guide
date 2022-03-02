package com.ooooo.core.request;

import lombok.Data;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Data
public class HttpProxyTemplateConfig {
	
	private String url;
	
	/**
	 * proxy the templateId of target
	 */
	private String proxyTemplateId;
}
