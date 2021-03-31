package com.ooooo.service;

import lombok.Data;

@Data
public class AccessTokenRequest {
	
	private String state;
	private String cookie;
	private String client_id;
	private String client_secret;
	private String redirect_uri;
	private String response_type = "code";
	private String scope;
	private String username;
	private String password;
	private String code;
	private String grant_type= "authorization_code";
	
	
}