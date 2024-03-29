package com.ooooo.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/23 18:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
	
	private String id;
	
	private Integer num;
}
