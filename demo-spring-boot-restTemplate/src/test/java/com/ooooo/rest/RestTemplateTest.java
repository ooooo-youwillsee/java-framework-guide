package com.ooooo.rest;

import static org.springframework.http.HttpMethod.POST;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * @author ooooo
 * @since 2021/06/07 23:23
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RestTemplateTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateTest.class);

  @Autowired
  private RestTemplate restTemplate;

  @LocalServerPort
  private int port;

  private String getUrl(String path) {
    return "http://127.0.0.1:" + port + path;
  }

  /**
   * testing for post form
   */
  @Test
  public void postForm() {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("id", "111");
    body.add("name", "postForm");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, httpHeaders);
    ResponseEntity<TestController.Resp> responseEntity = restTemplate.exchange(getUrl("/postForm"), POST, entity, TestController.Resp.class);
    HttpStatus statusCode = responseEntity.getStatusCode();
    Assertions.assertEquals(statusCode, HttpStatus.OK);
  }

  /**
   * testing for post json
   */
  @Test
  public void postJson() {
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("id", "111");
    body.add("name", "postJson");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<String> entity = new HttpEntity<>(JSON.toJSONString(body.toSingleValueMap()), httpHeaders);
    ResponseEntity<TestController.Resp> responseEntity = restTemplate.exchange(getUrl("/postJson"), POST, entity, TestController.Resp.class);
    HttpStatus statusCode = responseEntity.getStatusCode();
    Assertions.assertEquals(statusCode, HttpStatus.OK);
  }


  /**
   * testing for post json
   */
  @Test
  public void postFile() throws IOException {
    File tempFile = File.createTempFile("postFile", ".txt");
    StreamUtils.copy("testing for post file", StandardCharsets.UTF_8, Files.newOutputStream(tempFile.toPath()));

    HttpHeaders fileHeader = new HttpHeaders();
    fileHeader.add(HttpHeaders.CONTENT_DISPOSITION, "form-data; name=file; filename=postFile.txt");
    fileHeader.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
    HttpEntity<InputStreamResource> file = new HttpEntity<>(new InputStreamResource(Files.newInputStream(tempFile.toPath())), fileHeader);

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", file);
    body.add("key1", "value1");
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE);
    HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(body, httpHeaders);
    ResponseEntity<TestController.Resp> responseEntity = restTemplate.exchange(getUrl("/postFile"), POST, entity, TestController.Resp.class);
    HttpStatus statusCode = responseEntity.getStatusCode();
    Assertions.assertEquals(statusCode, HttpStatus.OK);
  }

  @Test
  public void testHttps() throws Exception {
    OkHttpClient client = new OkHttpClient.Builder()
        .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
        .build();
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

    ResponseEntity<String> entity = template.getForEntity("https://www.baidu.com", String.class);
    String body = entity.getBody();
    Assertions.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    LOGGER.info("testHttps: {}", body);
  }

}
