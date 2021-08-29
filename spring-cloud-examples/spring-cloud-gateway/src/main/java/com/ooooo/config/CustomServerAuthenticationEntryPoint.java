package com.ooooo.config;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author leizhijie
 * @since 2021/3/16 13:48
 */
public class CustomServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
	
	@Override
	public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
		return Mono.defer(() -> {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			return response.writeWith(Mono.fromSupplier(() -> {
				Map<String, String> res = new HashMap<>();
				res.put("message", "无效的token");
				res.put("code", "9999");
				return response.bufferFactory().wrap(JSON.toJSONBytes(res));
			}));
		});
	}
}
