package com.ooooo.config;

import java.security.KeyPair;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

/**
 * @author leizhijie
 * @since 2021/3/13 22:43
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	private final UserDetailsService userDetailsService;
	private final AuthenticationManager authenticationManager;
	private final KeyPair keyPair;
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	public AuthorizationServerConfig(KeyPair keyPair, AuthenticationManager authenticationManager,
	                                 PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {
		this.authenticationManager = authenticationManager;
		this.keyPair = keyPair;
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}
	
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.checkTokenAccess("permitAll()")
		        .passwordEncoder(passwordEncoder)
		
		//.tokenKeyAccess("permitAll()")
		;
	}
	
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// in jdbc by h2
		clients.jdbc(dataSource())
		       .passwordEncoder(passwordEncoder);
		
		// in memory
		//clients.inMemory()
		//       .withClient("crh")
		//       .authorizedGrantTypes("authorization_code", "refresh_token")
		//       .secret("{noop}secret")
		//       .scopes("read")
		//       .autoApprove("true")
		//       .accessTokenValiditySeconds(600_000_000)
		//       .refreshTokenValiditySeconds(600_000_000)
		//       .redirectUris("http://localhost:8888/auth/oauth/code")
		//       .and()
		//;
	}
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager)
		         .accessTokenConverter(jwtAccessTokenConverter())
		         // 对于授权码模式不需要userDetailsService, 但是刷新token需要
		         .userDetailsService(userDetailsService)
		;
	}
	
	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(keyPair);
		return converter;
	}
	
	@Bean
	public DataSource dataSource() {
		EmbeddedDatabase db = new EmbeddedDatabaseBuilder()
				.generateUniqueName(true)
				.setType(EmbeddedDatabaseType.H2)
				.setScriptEncoding("UTF-8")
				.ignoreFailedDrops(true)
				.addScript("schema.sql")
				.addScripts("oauth_user_details.sql", "oauth_client_details.sql")
				.build();
		return db;
	}
	
}
