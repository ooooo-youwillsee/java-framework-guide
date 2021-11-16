package com.ooooo.autoconfigure.env.dev;

import com.ooooo.autoconfigure.env.AbstractSimplePropertySource;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.apache.commons.configuration2.reloading.ReloadingController;
import org.apache.commons.configuration2.reloading.ReloadingEvent;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;

import static com.ooooo.constant.EnvironmentConstants.ENV_PREFIX;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2020/11/15 10:47
 */
@Slf4j
@Order(1)
public class LocalPropertiesPropertySource extends AbstractSimplePropertySource implements InitializingBean {
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	private ReloadingFileBasedConfigurationBuilder<PropertiesConfiguration> builder;
	
	private static final String DEFAULT_LOCAL_PROPERTIES_PATH = "sysoptions.properties";
	
	public LocalPropertiesPropertySource() {
		super(ENV_PREFIX + "local_properties");
		log.debug("开发环境，启用[{}]配置", getClass());
	}
	
	@Override
	public void afterPropertiesSet() {
		Integer sysOptionsLoadInterval = getEnvironment().getProperty("dev.localPropertiesLoadInterval", Integer.class, 1);
		String sysOptionsPath = getEnvironment().getProperty("dev.localPropertiesPath", DEFAULT_LOCAL_PROPERTIES_PATH);
		File propertiesFile = new File(sysOptionsPath);
		if (!propertiesFile.exists()) {
			return;
		}
		
		// server boot will publish event
		publisher.publishEvent(new LocalProperitesReReloadingEvent(new Object(), propertiesFile.getAbsolutePath()));
		builder = new ReloadingFileBasedConfigurationBuilder<>(PropertiesConfiguration.class).configure(new Parameters().fileBased().setFile(propertiesFile));
		ReloadingController reloadingController = builder.getReloadingController();
		reloadingController.addEventListener(ReloadingEvent.ANY, e -> publisher.publishEvent(new LocalProperitesReReloadingEvent(e, propertiesFile.getAbsolutePath())));
		
		PeriodicReloadingTrigger trigger = new PeriodicReloadingTrigger(reloadingController, null, sysOptionsLoadInterval, SECONDS);
		trigger.start();
	}
	
	@Override
	public Object getProperty(@NonNull String name) {
		if (builder == null) return null;
		Configuration configuration = null;
		try {
			configuration = builder.getConfiguration();
			String config_value = configuration.getString(name);
			if (config_value != null) {
				return config_value;
			}
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	//  http 请求使用
	public Map<String, Object> getHttpDevProperty() {
		Map<String, Object> httpParams = new HashMap<>();
		if (builder == null) return httpParams;
		try {
			Configuration configuration = builder.getConfiguration();
			Iterator<String> keys = configuration.getKeys();
			while (keys.hasNext()) {
				String key = keys.next();
				if (key.startsWith(ENV_PREFIX)) {
					httpParams.put(key, configuration.getString(key));
				}
			}
		} catch (ConfigurationException e) {
			log.error(e.getMessage(), e);
		}
		return httpParams;
	}
	
	public static class LocalProperitesReReloadingEvent extends ApplicationEvent {
		
		@Getter
		private final String localPropertiesPath;
		
		public LocalProperitesReReloadingEvent(Object source, String localPropertiesPath) {
			super(source);
			this.localPropertiesPath = localPropertiesPath;
		}
	}
	
}
