package com.ooooo;

import com.ooooo.impl.EnvironmentPropertySource;
import com.ooooo.impl.LocalPropertiesPropertySource;
import com.ooooo.impl.RequestParamsPropertySource;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.List;


/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2020/11/15 10:47
 */
@Configuration
public class EnvironmentAutoConfiguration {

  public static final String ENV_PREFIX = "_env_";

  @Bean
  @ConditionalOnClass(DispatcherServlet.class)
  public RequestParamsPropertySource requestParamsPropertySource() {
    return new RequestParamsPropertySource();
  }

  @Bean
  @Profile("dev")
  public LocalPropertiesPropertySource localPropertiesPropertySource() {
    return new LocalPropertiesPropertySource();
  }
	
	@Bean
	public EnvironmentPropertySource environmentPropertySource() {
		return new EnvironmentPropertySource();
	}
	
	@Bean
	public CompositePropertySources compositePropertySources(ObjectProvider<List<AbstractSimplePropertySource>> sources) {
		return new CompositePropertySources(sources.getIfAvailable());
	}
}
