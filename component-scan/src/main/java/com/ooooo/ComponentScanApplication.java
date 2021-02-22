package com.ooooo;

import com.alibaba.fastjson.JSON;
import com.ooooo.annotation.XXXComponentScan;
import com.ooooo.entity.Req;
import com.ooooo.entity.Resp;
import com.ooooo.test.TestXXXService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@XXXComponentScan
@SpringBootApplication
public class ComponentScanApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ComponentScanApplication.class, args);
		
		log.info("start test XXXService");
		TestXXXService testXXXService = context.getBean(TestXXXService.class);
		Resp resp = testXXXService.test(new Req("1"));
		log.info("end test XXXService: resp: {}", JSON.toJSONString(resp));
	}
	
}
