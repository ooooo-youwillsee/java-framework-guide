package com.ooooo.annotation;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpMethod;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/24 16:01
 */
@Data
@NoArgsConstructor
@ToString
public class XRequestMappingMethodInfo {
	
	public XRequestMappingMethodInfo(String path, HttpMethod[] httpMethod) {
		this.path = path;
		this.httpMethod = httpMethod;
	}
	
	public String path;
	
	public HttpMethod[] httpMethod;
}
