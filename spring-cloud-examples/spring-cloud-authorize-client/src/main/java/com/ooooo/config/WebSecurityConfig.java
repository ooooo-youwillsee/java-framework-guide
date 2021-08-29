package com.ooooo.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author leizhijie
 * @since 2021/3/15 11:02
 */
@Configuration
@EnableOAuth2Sso
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		    // 暴露错误，防止无线重定向
		    .antMatchers("/error").permitAll()
		    .anyRequest().authenticated()
		    .and()
		    .csrf().disable()
		;
	}
	
}
