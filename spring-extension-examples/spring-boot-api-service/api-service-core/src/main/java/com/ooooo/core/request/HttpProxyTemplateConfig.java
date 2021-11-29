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
	 * 代理的目标templateId
	 */
	private String proxyTemplateId;
}
