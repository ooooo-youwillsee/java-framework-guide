package com.ooooo.config;

import com.ooooo.service.CglibUserService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/12/3 16:50
 * @since 1.0.0
 */
@Configuration
@Aspect
public class CglibProxyConfiguration {
	
	@Bean
	public CglibUserService cglibUserService() {
		return new CglibUserService();
	}
	
	
	@Around("execution(* com.ooooo.service.CglibUserService.*(..))")
	public Object process(ProceedingJoinPoint point) throws Throwable {
		System.out.println("@Around：执行目标方法之前...");
		Object o = point.proceed();
		System.out.println("@Around：执行目标方法之后...");
		return o;
	}
	
}
