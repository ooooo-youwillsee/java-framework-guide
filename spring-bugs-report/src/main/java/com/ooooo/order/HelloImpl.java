package com.ooooo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.Ordered;

/**
 * @author ooooo
 * @since 2021/06/09 18:24
 */
@Data
@AllArgsConstructor
public class HelloImpl implements IHello, Ordered {

	private String id;
	@Override
	public int getOrder() {
		return 3;
	}
}
