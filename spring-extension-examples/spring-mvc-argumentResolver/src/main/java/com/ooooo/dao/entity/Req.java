package com.ooooo.dao.entity;

import lombok.Data;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/22 21:49
 */
@Data
public class Req {
	
	private ReqHeader header;
	
	private ReqBody body;
	
}
