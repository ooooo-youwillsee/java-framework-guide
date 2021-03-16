package com.ooooo.config;

import com.alibaba.fastjson.JSON;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author leizhijie
 * @since 2021/3/16 13:41
 */
public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {
	
	@Override
	public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException e) {
		return Mono.defer(() -> {
			ServerHttpResponse response = exchange.getResponse();
			response.setStatusCode(HttpStatus.UNAUTHORIZED);
			response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			return response.writeWith(Mono.fromSupplier(() -> {
				Map<String, String> res = new HashMap<>();
				res.put("message", "你没有权限访问");
				res.put("code", "9999");
				return response.bufferFactory().wrap(JSON.toJSONBytes(res));
			}));
		});
	}
}
