package com.ooooo.dao.entity;

import java.util.Date;
import lombok.Data;

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
