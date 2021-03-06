package com.ooooo.entity;

import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author leizhijie
 * @since 2021/2/23 18:37
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User{
	
	private String id;
	
	private String name;
	
}
