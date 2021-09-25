package com.ooooo.config;

import com.alibaba.cloud.stream.binder.rocketmq.config.RocketMQComponent4BinderAutoConfiguration;
import com.ooooo.config.DisableSpringCloudStreamConfiguration.DisableSpringCloudStreamCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.cloud.stream.function.FunctionConfiguration;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author ooooo
 * @date 2021/09/25 15:53
 * @since 1.0.0
 */
@Configuration
@Conditional(DisableSpringCloudStreamCondition.class)
@EnableAutoConfiguration(exclude = {BindingServiceConfiguration.class, FunctionConfiguration.class, RocketMQComponent4BinderAutoConfiguration.class})
public class DisableSpringCloudStreamConfiguration {

	private static final String FUNCTION_CONFIG = "spring.cloud.stream.function.definition";

	/**
	 * @return true if disable spring cloud stream
	 */
	public static boolean disabled(Environment environment) {
		String functionDefinitionConfig = environment.getProperty(FUNCTION_CONFIG);
		return StringUtils.isBlank(functionDefinitionConfig);
	}

	static class DisableSpringCloudStreamCondition extends SpringBootCondition {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			Environment environment = context.getEnvironment();
			boolean matched = disabled(environment);
			String message = matched ? "disabled" : "enabled";
			return new ConditionOutcome(matched, message + "spring cloud stream");
		}


	}
}
