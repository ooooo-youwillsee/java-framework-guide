package com.ooooo.core.request;

import java.util.Map;

/**
 * 动态参数接口
 *
 * @author leizhijie
 * @since 2021/4/8 11:12
 */
public interface IRequest {
	
	Map<String, Object> getParams();
}
