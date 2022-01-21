package com.ooooo.rest;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ooooo
 * @since 2021/06/07 23:25
 */
@Slf4j
@RestController
public class TestController {

	@PostMapping("/postForm")
	public Resp postForm(Req req) {
		log.info("postForm: {}", JSON.toJSONString(req));
		return new Resp("18", 1);
	}

	@PostMapping("/postJson")
	public Resp postJson(@RequestBody Req req) {
		log.info("postJson: {}", JSON.toJSONString(req));
		return new Resp("18", 1);
	}

	@PostMapping("/postFile")
	public Resp postFile(@RequestParam("file") MultipartFile file, String key1) throws IOException {
		log.info("postFile, key: {}, value: '{}'", file.getName(), new String(file.getBytes(), StandardCharsets.UTF_8));
		log.info("postFile, key: {}, value: '{}'", "key1", key1);
		return new Resp("18", 1);
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Req {
		private String name;
		private Integer id;
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class Resp {
		private String age;
		private Integer id;
	}
}
