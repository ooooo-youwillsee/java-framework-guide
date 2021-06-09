package com.ooooo.config;

import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author leizhijie
 * @since 2021/3/13 11:58
 */
@EnableWebFluxSecurity
public class WebSecurityConfig {
	
	@Autowired
	private KeyPair keyPair;
	
	@Bean
	// 由于httpBasic().disable()，默认没有用到
	public MapReactiveUserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder()
		                       .username("user")
		                       .password("password")
		                       .roles("USER")
		                       .build();
		return new MapReactiveUserDetailsService(user);
	}
	
	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				.authorizeExchange()
				.pathMatchers("/**").permitAll()
				//.pathMatchers("/auth/**").permitAll()
				//.pathMatchers("/api/**").hasAuthority("SCOPE_api")
				//.anyExchange().authenticated()
				.and()
				.httpBasic().disable()
				.csrf().disable()
				.formLogin().disable()
				.oauth2ResourceServer()
				.jwt()
				.publicKey((RSAPublicKey) keyPair.getPublic())
				.and()
				.authenticationEntryPoint(serverAuthenticationEntryPoint())
				.accessDeniedHandler(accessDeniedHandler())
		;
		
		return http.build();
	}
	
	@Bean
	public CustomAccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}
	
	@Bean
	public CustomServerAuthenticationEntryPoint serverAuthenticationEntryPoint() {
		return new CustomServerAuthenticationEntryPoint();
	}
}
