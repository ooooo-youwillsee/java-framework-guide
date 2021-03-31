package com.ooooo.service;

import lombok.Data;

@Data
public class AccessTokenResponse {
	
	private String access_token;
	private String token_type;
	private String refresh_token;
	private String expires_in;
	private String scope;
	private String jti;
	
	
}