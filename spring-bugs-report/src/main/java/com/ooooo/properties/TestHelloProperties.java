package com.ooooo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author ooooo
 * @since 2021/07/11 15:43
 */
@Data
@ConfigurationProperties(prefix = "test")

//@EnableConfigurationProperties(TestHelloProperties.class) that's will throw ab Exception
public class TestHelloProperties {

	private String id;

	private String name;

}
