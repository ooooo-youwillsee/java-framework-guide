package com.ooooo.core.request;

import com.alibaba.fastjson.JSON;
import com.ooooo.core.constants.ServiceType;
import com.ooooo.core.exception.APIException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * <p>create proxyItemplate using http or https</p>
 * <p>handle response</p>
 *
 * @author leizhijie
 * @since 1.0.0
 */
public class HttpProxyRestTemplateHelper {
    
    private static final String requestPrefix = "call/{serviceType}/{templateId}/{functionId}";
    
    public static final String PROXY_TEMPLATE_ID_KEY = "_PROXY_TEMPLATE_ID";
    
    private static final ConcurrentMap<String/* url */, RestTemplate> restTemplateMap = new ConcurrentHashMap<>();
    
    public static RestTemplate getRestTemplate(String url) {
        if (url == null) {
            throw new APIException("url is null");
        }
        return restTemplateMap.computeIfAbsent(url, __ -> getTemplate(url));
    }
    
    @NotNull
    private static RestTemplate getTemplate(String url) {
        OkHttpClient client = null;
        if (isHttps(url)) {
            client = new OkHttpClient.Builder()
                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
                .build();
        } else {
            client = new OkHttpClient.Builder().build();
        }
        
        OkHttp3ClientHttpRequestFactory requestFactory = new OkHttp3ClientHttpRequestFactory(client);
        requestFactory.setReadTimeout(60 * 1000 * 3);
        requestFactory.setConnectTimeout(1000 * 10);
        RestTemplate template = new RestTemplate(requestFactory);
        // utf8
        template.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        // 可以接受其他类型的状态码， 例如400
        template.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
            
            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
        return template;
    }
    
    public static URI buildURI(String url, AbstractRequestEntity<?> request, String configProxyTemplateId) {
        ServiceType serviceType = request.getServiceType();
        String proxyTemplateId = (String) request.getParams().get(PROXY_TEMPLATE_ID_KEY);
        if (proxyTemplateId == null) {
            if (configProxyTemplateId == null) {
                throw new IllegalArgumentException("both param['" + PROXY_TEMPLATE_ID_KEY + "'] and config['proxy_template_id'] aren't set when calling proxy");
            }
            proxyTemplateId = configProxyTemplateId;
        }
        String functionId = request.getUrl();
        return buildURI(url, serviceType, proxyTemplateId, functionId);
    }
    
    
    public static URI buildURI(String url, ServiceType serviceType, String templateId, String functionId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                                                           .path(
                                                               requestPrefix.replace("{serviceType}", serviceType.getType())
                                                                            .replace("{templateId}", templateId)
                                                                            .replace("{functionId}", functionId)
                                                           );
        return builder.encode(StandardCharsets.UTF_8).build().toUri();
    }
    
    public static HttpEntity<String> buildHttpEntity(AbstractRequestEntity<?> request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        Map<String, Object> params = request.getParams();
        params.put("_FUNCTION_NAME", request.getApiMappingNote());
        HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(params), httpHeaders);
        return httpEntity;
    }
    
    public static <T> HttpProxyResult<T> request(RestTemplate restTemplate, URI uri, HttpEntity<?> httpEntity) {
        ResponseEntity<HttpProxyResult<T>> entity = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<HttpProxyResult<T>>() {});
        HttpProxyResult<T> response = entity.getBody();
        if (!response.isSuccess()) {
            throw new APIException(response.error.getError_info());
        }
        
        return response;
    }
    
    public static boolean isHttps(String url) {
        return url != null && url.contains("https://");
    }
    
    
    @Data
    public static class HttpProxyResult<T> {
        
        private Error error = Error.SUCCESS;
        
        private T data;
        
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Error {
            
            public static final Error SUCCESS = new Error("0", "ok");
            
            private String error_no;
            private String error_info;
        }
        
        public boolean isSuccess() {
            return this.error.getError_no().equals("0");
        }
    }
    
}
