package com.ooooo.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author ooooo
 * @date 2021/09/21 15:11
 * @since 1.0.0
 */
@Data
public class Order {

	private Long id;

	private String userId;

	private Date payDate;
}
