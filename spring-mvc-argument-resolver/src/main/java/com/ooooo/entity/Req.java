package com.ooooo.entity;

import lombok.Data;

/**
 * @author leizhijie
 * @since 2021/2/22 21:49
 */
@Data
public class Req {
	
	private ReqHeader header;
	
	private ReqBody body;
	
}
