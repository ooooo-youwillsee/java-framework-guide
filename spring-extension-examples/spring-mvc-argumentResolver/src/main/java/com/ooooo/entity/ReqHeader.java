package com.ooooo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqHeader {
	
	private long timeStamp;
	
	private String requestId;
}
	