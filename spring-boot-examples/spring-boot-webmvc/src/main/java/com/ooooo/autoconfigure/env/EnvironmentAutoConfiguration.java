package com.ooooo.autoconfigure.env;

import com.ooooo.autoconfigure.env.dev.EnvironmentPropertySource;
import com.ooooo.autoconfigure.env.dev.LocalPropertiesPropertySource;
import com.ooooo.autoconfigure.env.dev.RequestParamsPropertySource;
import java.util.List;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.DispatcherServlet;

import static com.ooooo.constant.EnvironmentConstants.PROFILE_DEV;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2020/11/15 10:47
 */
@Configuration
public class EnvironmentAutoConfiguration {
	
	@Bean
	@ConditionalOnClass(DispatcherServlet.class)
	public RequestParamsPropertySource requestParamsPropertySource() {
		return new RequestParamsPropertySource();
	}
	
	@Bean
	@Profile(PROFILE_DEV)
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
