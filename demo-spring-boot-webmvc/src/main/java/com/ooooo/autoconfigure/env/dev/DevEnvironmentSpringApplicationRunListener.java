package com.ooooo.autoconfigure.env.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

import static com.ooooo.constant.EnvironmentConstants.PROFILE_DEV;
import static org.springframework.boot.context.config.ConfigFileApplicationListener.CONFIG_LOCATION_PROPERTY;
import static org.springframework.core.env.Profiles.of;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/15 13:27
 * @since 1.0
 */
public class DevEnvironmentSpringApplicationRunListener implements SpringApplicationRunListener, Ordered {
	
	private final String[] extensionFileNames = new String[]{"./application-dev", "../application-dev", "./bootstrap", "../bootstrap"};
	
	public DevEnvironmentSpringApplicationRunListener(SpringApplication application, String[] args) {}
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
	
	public void environmentPrepared(ConfigurableEnvironment environment) {
		if (!environment.acceptsProfiles(of(PROFILE_DEV))) return;
		setExtensionLocation(environment);
		//addExtensionFile(environment);
	}
	
	private void setExtensionLocation(ConfigurableEnvironment environment) {
    // 此时log还没有初始化好
    System.out.println("开发环境，重新设置[spring.config.location]属性");
    Map<String, Object> params = new HashMap<>();
    params.put(CONFIG_LOCATION_PROPERTY, "file:../../,file:../,file:../config/,classpath:/,classpath:/config/,file:./,file:./config/*/,file:./config/");
    environment.getPropertySources().addLast(new MapPropertySource("dev", params));
  }
	
}
