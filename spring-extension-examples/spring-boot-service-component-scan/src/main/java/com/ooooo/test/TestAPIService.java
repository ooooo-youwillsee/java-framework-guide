package com.ooooo.test;

import com.ooooo.annotation.APIMapping;
import com.ooooo.annotation.APIService;
import com.ooooo.dao.entity.Req;
import com.ooooo.dao.entity.Resp;
import org.springframework.http.HttpMethod;

/**
 * @author leizhijie
 * @since 2021/2/22 15:19
 */
@APIService
public interface TestAPIService {
	
	@APIMapping(value = "/test", method = HttpMethod.POST)
	Resp test(Req req);
}
