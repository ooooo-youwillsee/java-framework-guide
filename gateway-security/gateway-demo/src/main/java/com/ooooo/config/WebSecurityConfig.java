package com.ooooo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * @author leizhijie
 * @since 2021/3/13 11:58
 */
@EnableWebFluxSecurity
public class WebSecurityConfig {
	
	@Bean
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
				.anyExchange().authenticated()
				.and()
				.httpBasic().and()
				.formLogin().disable()
				.csrf().disable()
				.oauth2Login()
				//.oauth2ResourceServer()
				//.jwt()
		;
		
		return http.build();
	}
	
	
	//@Bean
	//public AuthenticationManager authenticationManager() {
	//	new ProviderManager(new DaoAuthenticationProvider())
	//}
}
