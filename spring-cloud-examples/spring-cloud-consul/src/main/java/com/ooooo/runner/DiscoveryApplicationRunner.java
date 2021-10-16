package com.ooooo.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ooooo
 * @date 2021/10/16 11:08
 * @since 1.0.0
 */
@Slf4j
@Component
public class DiscoveryApplicationRunner implements ApplicationRunner {

	@Autowired
	private DiscoveryClient discoveryClient;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		for (String serviceId : discoveryClient.getServices()) {
			log.info("serviceId: {}", serviceId);
			List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
			instances.forEach(serviceInstance -> {
				log.info("\t serviceInstanceId: {}, serviceInstanceHost: {}, serviceInstancePort: {}", serviceInstance.getInstanceId(), serviceInstance.getHost(), serviceInstance.getPort());
			});
		}
	}
}
