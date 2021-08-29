package com.ooooo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author ooooo
 * @date 2021/08/28 10:01
 */
@FeignClient(name = "api-demo")
public interface APIFeignClient {

	@GetMapping("/test")
	String test(@RequestParam("name") String name);
}
