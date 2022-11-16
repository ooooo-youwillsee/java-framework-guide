package com.ooooo.unsafe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnsafeUser {

  private Long id;

  private String name;

  private Integer age;

}
