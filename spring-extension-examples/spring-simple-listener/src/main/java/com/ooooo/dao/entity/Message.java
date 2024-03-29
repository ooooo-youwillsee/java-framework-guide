package com.ooooo.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/23 17:25
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Message<T> {
	
	private T data;
	
	public static Message<?> payload(Object o) {
		return new Message<>(o);
	}
}
