package com.ooooo.controller;


import com.alibaba.fastjson.JSON;
import com.ooooo.SpringMvcArgumentResolverApplicationTests;
import com.ooooo.dao.entity.Req;
import com.ooooo.dao.entity.ReqBody;
import com.ooooo.dao.entity.ReqHeader;
import com.ooooo.dao.entity.User;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/22 22:08
 */
@AutoConfigureMockMvc
public class UserControllerTests extends SpringMvcArgumentResolverApplicationTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserControllerTests.class);
    @Autowired
	private MockMvc mockMvc;
	
	@Test
	public void user() throws Exception {
		String requestJson = JSON.toJSONString(createReq());
		LOGGER.info("requestJson: {}", requestJson);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/user")
		                                                            .accept(MediaType.APPLICATION_JSON)
		                                                            .contentType(MediaType.APPLICATION_JSON)
		                                                            .content(requestJson))
		                                                            .andReturn();
		String res = mvcResult.getResponse().getContentAsString();
		Assert.notNull(res, "message cann't is null");
	}
	
	
	private Req createReq() {
		Req req = new Req();
		ReqBody reqBody = new ReqBody();
		reqBody.put("user", new User("1", "tom"));
		req.setHeader(new ReqHeader(new Date().getTime(), "123456"));
		req.setBody(reqBody);
		return req;
	}
}
