package com.ooooo.service;

import lombok.Data;

@Data
public class RefreshTokenRequest {
	
	private String grant_type = "refresh_token";
	private String client_id;
	private String client_secret;
	private String refresh_token;
	
	
}