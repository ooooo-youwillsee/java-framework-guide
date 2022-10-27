package com.ooooo.auth.config;

import com.ooooo.auth.authentication.AccessTokenFilter;
import com.ooooo.auth.authentication.AuthorizationEndpoint;
import com.ooooo.auth.authentication.DefaultSecurityService;
import com.ooooo.auth.authentication.SecurityService;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <a href='https://docs.spring.io/spring-security/reference/servlet/architecture.html'>spring security doc</a>
 *
 * @author leizhijie
 * @date 2022/3/29 10:46
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {


  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .mvcMatchers("/authless/**")
        .mvcMatchers(AuthorizationEndpoint.TOKEN_URL)
        .and()
    ;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().authenticated().and()
        .httpBasic().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .csrf().disable()
        .anonymous().disable()
        .addFilterBefore(accessTokenFilter(), UsernamePasswordAuthenticationFilter.class)
    // .exceptionHandling().accessDeniedHandler()
    ;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // override, then config
    auth.inMemoryAuthentication();
  }

  @Bean
  public AccessTokenFilter accessTokenFilter() {
    return new AccessTokenFilter();
  }

  @Bean
  @ConditionalOnMissingBean
  public SecurityService defaultSecurityService() {
    return new DefaultSecurityService();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(0, convertedAuthenticationPrincipalArgumentResolver());
  }

  @Bean
  public ConvertedAuthenticationPrincipalArgumentResolver convertedAuthenticationPrincipalArgumentResolver() {
    return new ConvertedAuthenticationPrincipalArgumentResolver();
  }
}
