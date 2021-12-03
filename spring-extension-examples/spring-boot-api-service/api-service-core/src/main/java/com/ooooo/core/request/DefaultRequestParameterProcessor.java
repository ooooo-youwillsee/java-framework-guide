package com.ooooo.core.request;

import com.ooooo.core.annotation.ServiceTypeAware;
import java.util.Map;

public interface DefaultRequestParameterProcessor extends ServiceTypeAware {
	
	default Map<String, Object> getValues(String url, String name) {
		return null;
	}
	
}
