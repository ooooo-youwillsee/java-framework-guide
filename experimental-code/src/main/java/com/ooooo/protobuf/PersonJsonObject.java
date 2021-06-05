package com.ooooo.protobuf;

import lombok.Data;

/**
 * Person 类对应的 实体类
 * @author leizhijie
 * @see PersonOrBuilder
 * @since 2021/6/5 12:56
 */
@Data
public class PersonJsonObject {
	
	private String name;
	private int id;
	private String email;
	
}
