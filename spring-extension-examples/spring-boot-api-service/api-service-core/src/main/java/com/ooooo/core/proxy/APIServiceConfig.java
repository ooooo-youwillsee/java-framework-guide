package com.ooooo.core.proxy;

import com.ooooo.core.constants.ServiceType;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.aop.Advice;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/19 20:02
 */
public class APIServiceConfig {

    @Setter
    @Getter
    private ApplicationContext applicationContext;
	
	@Setter
	@Getter
	private ServiceType serviceType;
	
	@Getter
	private final List<Advice> tmpAdvices = new CopyOnWriteArrayList<>();
	
}
