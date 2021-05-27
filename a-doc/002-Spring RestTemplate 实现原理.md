# RestTemplate 实现原理

示例：

```java 
// 可以使用不同的 requestFactory
OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory();
requestFactory.setReadTimeout(60 * 1000 * 3);
requestFactory.setConnectTimeout(1000 * 10);
RestTemplate template = new RestTemplate(requestFactory);
// utf8
template.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
// 可以设置通用的头，拦截配置
template.setInterceptors(Collections.singletonList(
    (request, body, execution) -> {
    request.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    request.getHeaders().add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    return execution.execute(request, body);
}));
// 可以接受其他类型的状态码， 例如400
template.setErrorHandler(new ResponseErrorHandler() {
    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return false;
   }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
    }
});
```


## 核心消息转换器

1. `AllEncompassingFormHttpMessageConverter`    

- 这个处理了 `form` 和 `multipart` 的请求。 源码位置 `org.springframework.http.converter.FormHttpMessageConverter#canRead`， 要求 `body` 参数必须为 `MultiValueMap` 类型，和相应的头信息。
  
- 在构造方法中 `partConverters` 属性明确了 value 支持的类型，如果是上传文件，必须要为 `Resource` 或者 `byte[]` 类型。

- 这个字符集是 `StandardCharsets.UTF_8`。


2. `StringHttpMessageConverter` 

默认是 `StandardCharsets.ISO_8859_1` 字符集。



## 关键源码分析


```java 
public <T> T execute(String url, HttpMethod method, @Nullable RequestCallback requestCallback,
        @Nullable ResponseExtractor<T> responseExtractor, Object... uriVariables) throws RestClientException
```

1. `getRequestFactory().createRequest(url, method)` 由不同的 `ClientHttpRequestFactory` 实现（比如 okHttp 、SimpleClientHttp）来创建请求 `request` 对象。
   
2. `RequestCallback` 执行 `requestCallback.doWithRequest(request)` 方法， 对请求体进行操作，包括对**表单**，**文件**的请求参数操作。
   
3. `request.execute()` 执行请求得到 `response`。
   
4. `errorHandler.hasError(response)` 判断是否有错误，默认情况下响应码为 `4xx` 和 `5xx` 作为错误， 可以用 `restTemplate.setErrorHandler` 来设置，如果有错误，就抛出异常。
   
5. `responseExtractor.extractData(response)` 对结果进行处理， 默认实现为 `ResponseEntityResponseExtractor` (利用 `messageConverters` 对 `ResponseType` 进行转换)。