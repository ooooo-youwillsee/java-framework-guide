package com.ooooo.core.generic;

import com.ooooo.core.constants.CounterConstants;
import com.ooooo.core.request.AbstractRequestEntity;
import com.ooooo.core.request.IRequest;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
@AllArgsConstructor
public class GeneriRequest implements IRequest {
	
	/**
	 * invoker 中实际选择的templateId 如果为空，用默认策略
	 *
	 * @see AbstractRequestEntity#buildTemplateId()
	 */
	private String templateId;
	
	/**
	 * 调用的url， 也就是功能号
	 */
	private String url;
	
	private Map<String, Object> params = new HashMap<>();
	
	@Override
	public Map<String, Object> getParams() {
		if (StringUtils.isNotBlank(templateId)) {
			params.put(CounterConstants.TEMPLATE_ID_KEY, templateId);
		}
		if (StringUtils.isNotBlank(url)) {
			params.put(CounterConstants.GENERIC_SERVICE_URL_KEY, url);
		}
		return params;
	}
}