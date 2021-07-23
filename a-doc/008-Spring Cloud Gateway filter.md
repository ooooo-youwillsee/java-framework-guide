# Spring Cloud Gateway filter

## 1. modify response of downstream service (example: decrypt the response )

```java
@Slf4j
@Component
public class DecryptFilter implements GlobalFilter, Ordered {
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerHttpResponse response = exchange.getResponse();
		DataBufferFactory dataBufferFactory = response.bufferFactory();
		ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
			@Override
			public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
				if (response.getHeaders().getContentType() == null || !response.getHeaders().getContentType().includes(MediaType.APPLICATION_JSON)) {
					return chain.filter(exchange);
				}
				
				if (!(body instanceof Flux)) {
					return super.writeWith(body);
				}
				
				Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;
				return super.writeWith(flux.buffer().map(dataBuffers -> {
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					dataBuffers.forEach(buffer -> {
						byte[] array = new byte[buffer.readableByteCount()];
						buffer.read(array);
						try {
							outputStream.write(array);
						} catch (IOException ignored) {
						}
						DataBufferUtils.release(buffer);
					});
					byte[] write = decrypt(outputStream.toByteArray()).getBytes(StandardCharsets.UTF_8);
					response.getHeaders().setContentLength(write.length);
					return dataBufferFactory.wrap(write);
				}));
			}
		};
		ServerWebExchange serverWebExchange = exchange.mutate().response(decoratedResponse).build();
		return chain.filter(serverWebExchange);
	}
	
	@Override
	public int getOrder() {
		// -1 is response write filter, must be called before that
		return -2;
	}
	
	private String decrypt(byte[] buffer) {
	    //  no implement
		return "";
	}
}

```

