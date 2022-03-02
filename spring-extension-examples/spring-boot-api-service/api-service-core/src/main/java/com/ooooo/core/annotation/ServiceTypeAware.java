package com.ooooo.core.annotation;

import com.ooooo.core.constants.ServiceType;
import org.springframework.beans.factory.Aware;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface ServiceTypeAware extends Aware {
	
	ServiceType getServiceType();
}
