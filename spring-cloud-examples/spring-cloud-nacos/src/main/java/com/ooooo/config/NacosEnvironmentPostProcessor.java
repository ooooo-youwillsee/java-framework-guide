package com.ooooo.config;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.cloud.nacos.parser.NacosDataParserHandler;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.NacosConfigService;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.StringUtils;

/**
 * It was configured in META-INF/spring.factories
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class NacosEnvironmentPostProcessor implements EnvironmentPostProcessor {
	
	public String NACOS_CONFIG_ENABLED = "spring.cloud.nacos.config.enabled";
	
	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		Boolean enabled = environment.getProperty(NACOS_CONFIG_ENABLED, Boolean.class, false);
		if (enabled.equals(Boolean.FALSE)) {
			return;
		}
		
		Binder binder = Binder.get(environment);
		BindResult<NacosConfigProperties> bindResult = binder.bind(NacosConfigProperties.PREFIX, NacosConfigProperties.class);
		NacosConfigProperties nacosConfigProperties = bindResult.orElseGet(NacosConfigProperties::new);
		nacosConfigProperties.setEnvironment(environment);
		nacosConfigProperties.init();
		
		try {
			NacosConfigService nacosConfigService = new NacosConfigService(nacosConfigProperties.assembleConfigServiceProperties());
			MutablePropertySources mutablePropertySources = environment.getPropertySources();
			nacosConfigProperties.getSharedConfigs()
			                     .forEach(sharedConfig -> {
				                              loadNacosData(nacosConfigService, sharedConfig.getDataId(), sharedConfig.getGroup(), FilenameUtils.getExtension(sharedConfig.getDataId()), nacosConfigProperties.getTimeout()).forEach(mutablePropertySources::addLast);
			                              }
			                     );
			nacosConfigService.shutDown();
		} catch (NacosException ignored) {
			System.out.println(getClass() + " initialize failure !");
			log.info(getClass() + " initialize failure !");
		}
	}
	
	
	private List<PropertySource<?>> loadNacosData(ConfigService configService, String dataId, String group,
	                                              String fileExtension, long timeout) {
		String data = null;
		try {
			data = configService.getConfig(dataId, group, timeout);
			if (StringUtils.isEmpty(data)) {
				log.warn(
						"Ignore the empty nacos configuration and get it based on dataId[{}] & group[{}]",
						dataId, group);
				return Collections.emptyList();
			}
			if (log.isDebugEnabled()) {
				log.debug(String.format(
						"Loading nacos data, dataId: '%s', group: '%s', data: %s", dataId,
						group, data));
			}
			return NacosDataParserHandler.getInstance().parseNacosData(dataId, data,
			                                                           fileExtension);
		} catch (NacosException e) {
			log.error("get data from Nacos error,dataId:{} ", dataId, e);
		} catch (Exception e) {
			log.error("parse data from Nacos error,dataId:{},data:{}", dataId, data, e);
		}
		return Collections.emptyList();
	}
	
}
