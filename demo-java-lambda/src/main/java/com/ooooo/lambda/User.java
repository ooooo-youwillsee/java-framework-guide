package com.ooooo.lambda;

import lombok.Data;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/5/22 16:02
 */
@Data
public class User {

	private String username;

	public String getPassword(String password) {
		return password;
	}
}
