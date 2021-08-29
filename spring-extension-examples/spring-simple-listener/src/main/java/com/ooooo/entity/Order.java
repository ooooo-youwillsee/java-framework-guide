package com.ooooo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author leizhijie
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
