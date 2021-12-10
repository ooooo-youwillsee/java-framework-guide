package com.ooooo.core.request;

import lombok.Data;

/**
 * @author leizhijie
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
