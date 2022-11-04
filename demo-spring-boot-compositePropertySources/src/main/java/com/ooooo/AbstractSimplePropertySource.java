package com.ooooo;

import java.util.Collections;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2020/11/15 10:47
 */
@Slf4j
public abstract class AbstractSimplePropertySource extends PropertySource<Map<String, String>> implements EnvironmentAware {

  protected Environment environment;
	
	public AbstractSimplePropertySource(String name) {
		super(name, Collections.emptyMap());
	}
	
	public void setEnvironment(@NonNull Environment environment) {
		this.environment = environment;
	}
	
	public Environment getEnvironment() {
		return environment;
	}
	
}
