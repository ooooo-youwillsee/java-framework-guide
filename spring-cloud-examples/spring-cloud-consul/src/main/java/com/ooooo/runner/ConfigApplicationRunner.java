package com.ooooo.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author ooooo
 * @date 2021/10/16 12:08
 * @since 1.0.0
 */
@Slf4j
@Component
public class ConfigApplicationRunner implements ApplicationRunner {

	@Value("${aaa:111}")
	private String aaa;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("aaa: {}", aaa);
	}

}
