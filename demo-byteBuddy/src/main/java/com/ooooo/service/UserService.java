package com.ooooo.service;

import com.ooooo.dto.HelloRequest;
import com.ooooo.dto.HelloResponse;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class UserService {
  
  public HelloResponse sayHello(HelloRequest request) {
    HelloResponse helloResponse = new HelloResponse();
    helloResponse.setId("sayHello: " + request.getId());
    helloResponse.setName("sayHello: " + request.getName());
    return helloResponse;
  }
}
