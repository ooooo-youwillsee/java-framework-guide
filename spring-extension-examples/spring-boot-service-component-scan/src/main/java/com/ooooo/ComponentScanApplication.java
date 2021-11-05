package com.ooooo;

import com.alibaba.fastjson.JSON;
import com.ooooo.annotation.APIServiceComponentScan;
import com.ooooo.dao.entity.Req;
import com.ooooo.dao.entity.Resp;
import com.ooooo.test.TestAPIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@APIServiceComponentScan
@SpringBootApplication
public class ComponentScanApplication {
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ComponentScanApplication.class, args);
		
		log.info("start test APIService");
		TestAPIService testAPIService = context.getBean(TestAPIService.class);
		Resp resp = testAPIService.test(new Req("1"));
		log.info("end test APIService: resp: {}", JSON.toJSONString(resp));
	}
	
}
