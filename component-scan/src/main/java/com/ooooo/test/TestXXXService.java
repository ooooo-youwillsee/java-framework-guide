package com.ooooo.test;

import com.ooooo.annotation.XXXRequestMapping;
import com.ooooo.annotation.XXXService;
import com.ooooo.entity.Req;
import com.ooooo.entity.Resp;
import org.springframework.http.HttpMethod;

/**
 * @author leizhijie
 * @since 2021/2/22 15:19
 */
@XXXService
public interface TestXXXService {
	
	@XXXRequestMapping(value = "/test", method = HttpMethod.POST)
	Resp test(Req req);
}
