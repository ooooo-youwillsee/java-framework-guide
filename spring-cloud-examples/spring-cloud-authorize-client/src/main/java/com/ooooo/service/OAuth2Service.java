package com.ooooo.service;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author leizhijie
 * @since 2021/3/31 15:22
 */
@Service
public class OAuth2Service {
	
	@Autowired
	@Qualifier("oauth2RestTemplate")
	public RestTemplate restTemplate;
	
	private static final String url = "http://localhost:8888";
	private static final String authorize_url = "/auth/oauth/authorize";
	private static final String token_url = "/auth/oauth/token";
	private static final String testApi_url = "/api/test";
	
	
	/**
	 * 获取token
	 *
	 * @param request
	 * @return
	 */
	public AccessTokenResponse create(HttpServletRequest request) {
		AccessTokenRequest tokenRequest = new AccessTokenRequest();
		tokenRequest.setState("1");  // 回调时会返回原值
		tokenRequest.setClient_id("xxx");
		tokenRequest.setClient_secret("secret");
		//tokenRequest.setRedirect_uri(calculateCurrentUri(request));
		tokenRequest.setRedirect_uri("http://127.0.0.1:8986/login");
		tokenRequest.setScope("api");
		tokenRequest.setUsername("user");
		tokenRequest.setPassword("password");
		
		getAuthorizationCode(tokenRequest);
		AccessTokenResponse accessToken = getAccessToken(tokenRequest);
		return accessToken;
	}
	
	
	/**
	 * 刷新token
	 *
	 * @return
	 */
	public AccessTokenResponse refresh(HttpServletRequest request) {
		RefreshTokenRequest tokenRequest = new RefreshTokenRequest();
		String defaultToken = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2MTg5ODU0NDIsInVzZXJfbmFtZSI6InVzZXIiLCJqdGkiOiJlMWE0MDllMS00YmU5LTRkNDQtODI4Zi04Zjc1YTFmOWFjMDEiLCJjbGllbnRfaWQiOiJ4eHgiLCJzY29wZSI6WyJhcGkiXSwiYXRpIjoiYzc5YzQ0MzYtZDQ4OC00MGQwLWE4MGUtMzVmMjQ5YjE0ZjhmIn0.TZiduAVP-_enoUGnzL5rEKpN12-MKcjQz4TCvHCr_AKNcyJ4eTLXQvV0AcnLnhdcUVLoCNsOJ_jNVxA0ewwNVFBvW5ECqR4xTdEg1bWNdf8cqsKIyuE_Wkzm8OdOrVxDGc9FsxhazZl6Jh5C-K1kVv2jCv7tD1CJ-KGE9Yt8cei4_sqSehmxBmXf5enCphDHQdGnSXH8moLt-VKxhYuOqaVuKHGbZ3lPnkc2LWHkVs1a0kz3zGJlxUl6hlkS9V8gTuGucqQfrhRJnG-Q3hgV4EGJZYHn8Cy3tUBnY3H3WX_sjKcR2U1RAPI4Pqf7GOJrHgtFbNANZcvxr0YFFhuicg";
		tokenRequest.setClient_id("xxx");
		tokenRequest.setClient_secret("secret");
		String refresh_token = request.getParameter("refresh_token") == null ? defaultToken : request.getParameter("refresh_token");
		tokenRequest.setRefresh_token(refresh_token);
		
		AccessTokenResponse accessToken = getRefreshToken(tokenRequest);
		return accessToken;
	}
	
	
	/**
	 * 请求
	 *
	 * @return
	 */
	public String testApi(AccessTokenResponse tokenResponse) {
		URI uri = UriComponentsBuilder.fromUri(URI.create(url))
		                              .path(testApi_url)
		                              .build().encode().toUri();
		
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.add(HttpHeaders.AUTHORIZATION, "Bearer " + tokenResponse.getAccess_token());
		httpHeader.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity httpEntity = new HttpEntity(httpHeader);
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, String.class);
		return response.getBody();
	}
	
	
	private AccessTokenResponse getRefreshToken(RefreshTokenRequest tokenRequest) {
		URI uri = UriComponentsBuilder.fromUri(URI.create(url))
		                              .path(token_url)
		                              .queryParam("grant_type", tokenRequest.getGrant_type())
		                              .queryParam("client_id", tokenRequest.getClient_id())
		                              .queryParam("refresh_token", tokenRequest.getRefresh_token())
		                              .build().encode().toUri();
		
		HttpHeaders httpHeader = new HttpHeaders();
		String basic = "Basic " + Base64Utils.encodeToString((tokenRequest.getClient_id() + ":" + tokenRequest.getClient_secret()).getBytes(StandardCharsets.ISO_8859_1));
		httpHeader.add(HttpHeaders.AUTHORIZATION, basic);
		httpHeader.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		HttpEntity httpEntity = new HttpEntity(httpHeader);
		ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, AccessTokenResponse.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new IllegalArgumentException("getRefreshToken fail");
		}
		return response.getBody();
	}
	
	
	private void getAuthorizationCode(AccessTokenRequest tokenRequest) {
		URI uri = UriComponentsBuilder.fromUri(URI.create(url))
		                              .path(authorize_url)
		                              .queryParam("state", tokenRequest.getState())
		                              .queryParam("client_id", tokenRequest.getClient_id())
		                              .queryParam("redirect_uri", tokenRequest.getRedirect_uri())
		                              .queryParam("response_type", tokenRequest.getResponse_type())
		                              .queryParam("scope", tokenRequest.getScope())
		                              .build().encode().toUri();
		
		HttpHeaders httpHeader = new HttpHeaders();
		String basic = "Basic " + Base64.getEncoder().encodeToString((tokenRequest.getUsername() + ":" + tokenRequest.getPassword()).getBytes(StandardCharsets.ISO_8859_1));
		httpHeader.add(HttpHeaders.AUTHORIZATION, basic);
		HttpEntity httpEntity = new HttpEntity(httpHeader);
		ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, Void.class);
		if (response.getStatusCode() == HttpStatus.OK) {
			throw new IllegalArgumentException("Need to re-submit with approval...");
		}
		if (response.getHeaders().containsKey("Set-Cookie")) {
			tokenRequest.setCookie(response.getHeaders().getFirst("Set-Cookie"));
		}
		URI location = response.getHeaders().getLocation();
		String query = location.getQuery();
		Map<String, String> map = extractMap(query);
		if (map.containsKey("state")) {
			tokenRequest.setState(map.get("state"));
		}
		String code = map.get("code");
		if (code == null) {
			throw new IllegalArgumentException("code is null");
		}
		tokenRequest.setCode(code);
	}
	
	
	private AccessTokenResponse getAccessToken(AccessTokenRequest tokenRequest) {
		URI uri = UriComponentsBuilder.fromUri(URI.create(url))
		                              .path(token_url)
		                              .queryParam("grant_type", tokenRequest.getGrant_type())
		                              .queryParam("client_id", tokenRequest.getClient_id())
		                              .queryParam("code", tokenRequest.getCode())
		                              .queryParam("scope", tokenRequest.getScope())
		                              .queryParam("redirect_uri", tokenRequest.getRedirect_uri())
		                              .build().encode().toUri();
		
		HttpHeaders httpHeader = new HttpHeaders();
		String basic = "Basic " + Base64Utils.encodeToString((tokenRequest.getClient_id() + ":" + tokenRequest.getClient_secret()).getBytes(StandardCharsets.ISO_8859_1));
		httpHeader.add(HttpHeaders.AUTHORIZATION, basic);
		httpHeader.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
		if (!StringUtils.isEmpty(tokenRequest.getCookie())) {
			httpHeader.add("Cookie", tokenRequest.getCookie());
		}
		HttpEntity httpEntity = new HttpEntity(httpHeader);
		ResponseEntity<AccessTokenResponse> response = restTemplate.exchange(uri, HttpMethod.POST, httpEntity, AccessTokenResponse.class);
		if (response.getStatusCode() != HttpStatus.OK) {
			throw new IllegalArgumentException("getAccessToken fail");
		}
		return response.getBody();
	}
	
	
	public static Map<String, String> extractMap(String query) {
		Map<String, String> map = new HashMap<String, String>();
		Properties properties = StringUtils.splitArrayElementsIntoProperties(
				StringUtils.delimitedListToStringArray(query, "&"), "=");
		if (properties != null) {
			for (Object key : properties.keySet()) {
				map.put(key.toString(), properties.get(key).toString());
			}
		}
		return map;
	}
	
	
	protected String calculateCurrentUri(HttpServletRequest request) {
		ServletUriComponentsBuilder builder = ServletUriComponentsBuilder
				.fromRequest(request);
		String queryString = request.getQueryString();
		boolean legalSpaces = queryString != null && queryString.contains("+");
		if (legalSpaces) {
			builder.replaceQuery(queryString.replace("+", "%20"));
		}
		UriComponents uri = null;
		try {
			uri = builder.replaceQueryParam("code").build(true);
		} catch (IllegalArgumentException ex) {
			return null;
		}
		String query = uri.getQuery();
		if (legalSpaces) {
			query = query.replace("%20", "+");
		}
		return ServletUriComponentsBuilder.fromUri(uri.toUri())
		                                  .replaceQuery(query).build().toString();
	}
}
