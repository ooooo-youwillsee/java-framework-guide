package com.ooooo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloRequest {
  
  private String id;
  
  private String name;
}
