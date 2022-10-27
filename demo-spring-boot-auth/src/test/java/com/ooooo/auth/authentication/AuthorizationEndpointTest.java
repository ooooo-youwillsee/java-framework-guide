package com.ooooo.auth.authentication;

import static com.ooooo.auth.authentication.AccessTokenAuthentication.getAuthorities;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ooooo.auth.AuthApplicationTests;
import com.ooooo.auth.test.AuthTestController.Result;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @author leizhijie
 * @date 2022/3/30 17:35
 * @since 1.0.0
 */
@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(classes = AuthApplicationTests.class)
class AuthorizationEndpointTest {


  @Autowired
  private MockMvc mockMvc;

  @SneakyThrows
  @Test
  public void test403() {
    MvcResult mvcResult = mockMvc.perform(
        // the requested url [/403] isn't exist
        MockMvcRequestBuilders.request(HttpMethod.POST, "/403")
    ).andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();
    log.info("status: {}", response.getStatus());
    log.info("content: {}", response.getContentAsString());
    log.info("errorMessage: {}", response.getErrorMessage());

    assertEquals(403, response.getStatus());
  }

  @SneakyThrows
  @Test
  public void testGetToken() {
    Map<String, Object> params = new HashMap<>();
    params.put("username", "tom");
    params.put("password", "123");

    MvcResult mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.request(HttpMethod.POST, AuthorizationEndpoint.TOKEN_URL)
            .content(JSON.toJSONString(params))
            .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();

    log.info("status: {}", response.getStatus());
    log.info("content: {}", response.getContentAsString());
    log.info("errorMessage: {}", response.getErrorMessage());

    assertEquals(200, response.getStatus());
    Assertions.assertNull(response.getErrorMessage());
  }


  /**
   * 请求, 用 json 形式的 accessToken
   */
  @SneakyThrows
  @ValueSource(booleans = {false, true})
  @ParameterizedTest
  public void testRequestForJsonAccessToken(boolean base64) {
    Map<String, Object> userInfo = new HashMap<>();
    userInfo.put("client_id", "213213");
    userInfo.put("client_name", "haha");

    // build a json accessToken
    AccessTokenAuthentication authentication = new AccessTokenAuthentication();
    authentication.setPrincipal(userInfo);
    authentication.setAuthorities(getAuthorities(Collections.singletonList("ADMIN")));
    authentication.setUniqueId("tom:123");
    authentication.setExpiresAt(DateUtils.addHours(new Date(), 1));
    String accessToken = "{json}" + JSON.toJSONString(authentication);
    if (base64) {
      accessToken = "{base64}" + Base64.encodeBase64String(accessToken.getBytes(StandardCharsets.UTF_8));
    }

    // request url for /403
    MvcResult mvcResult = mockMvc.perform(
        // the requested url [/403] isn't exist
        MockMvcRequestBuilders.request(HttpMethod.POST, "/auth/test1")
            .header(HttpHeaders.AUTHORIZATION, accessToken)
            .content(JSON.toJSONString(new HashMap<>()))
            .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();
    log.info("status: {}", response.getStatus());
    log.info("content: {}", response.getContentAsString());
    log.info("errorMessage: {}", response.getErrorMessage());

    assertEquals(200, response.getStatus());
  }


  /**
   * 先获取 accessToken, 然后再请求
   */
  @SneakyThrows
  @Test
  public void testGetTokenThenRequest() {
    Map<String, Object> params = new HashMap<>();
    params.put("username", "tom");
    params.put("password", "123");

    MvcResult mvcResult = mockMvc.perform(
        MockMvcRequestBuilders.request(HttpMethod.POST, AuthorizationEndpoint.TOKEN_URL)
            .content(JSON.toJSONString(params))
            .contentType(MediaType.APPLICATION_JSON)
            .locale(Locale.CHINA)
    ).andReturn();

    String content = mvcResult.getResponse().getContentAsString();
    AccessToken accessToken = JSON.parseObject(content, new TypeReference<Result<AccessToken>>() {}).getData();

    // request url for /auth/test1
    mvcResult = mockMvc.perform(
        // the requested url [/403] isn't exist
        MockMvcRequestBuilders.request(HttpMethod.POST, "/auth/test1")
            .header(HttpHeaders.AUTHORIZATION, accessToken.getAccessToken())
            .content(JSON.toJSONString(new HashMap<>()))
            .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    MockHttpServletResponse response = mvcResult.getResponse();
    log.info("status: {}", response.getStatus());
    log.info("content: {}", response.getContentAsString());
    log.info("errorMessage: {}", response.getErrorMessage());

    assertEquals(200, response.getStatus());

    // request url for /auth/test2
    mvcResult = mockMvc.perform(
        // the requested url [/403] isn't exist
        MockMvcRequestBuilders.request(HttpMethod.POST, "/auth/test2")
            .header(HttpHeaders.AUTHORIZATION, accessToken.getAccessToken())
            .content(JSON.toJSONString(new HashMap<>()))
            .contentType(MediaType.APPLICATION_JSON)
    ).andReturn();

    response = mvcResult.getResponse();
    log.info("status: {}", response.getStatus());
    log.info("content: {}", response.getContentAsString());
    log.info("errorMessage: {}", response.getErrorMessage());

    assertEquals(200, response.getStatus());
  }

}