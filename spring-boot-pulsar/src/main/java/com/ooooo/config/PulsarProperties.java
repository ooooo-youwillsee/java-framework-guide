package com.ooooo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ooooo
 * @date 2021/10/10 09:41
 * @since 1.0.0
 */
@Data
@ConfigurationProperties(prefix = "pulsar")
public class PulsarProperties {

	public String serviceUrl;
}
