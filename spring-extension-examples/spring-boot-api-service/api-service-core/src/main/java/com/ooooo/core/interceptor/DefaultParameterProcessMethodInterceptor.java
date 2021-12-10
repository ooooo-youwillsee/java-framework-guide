package com.ooooo.core.interceptor;


import com.ooooo.core.constants.InterceptorType;
import com.ooooo.core.context.APIServiceContext;
import com.ooooo.core.proxy.APIServiceConfig;
import com.ooooo.core.request.AbstractRequestEntity;
import com.ooooo.core.request.DefaultRequestParameterProcessor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;

import static com.ooooo.core.constants.CounterConstants.REQUEST_ENTITY_KEY;

public final class DefaultParameterProcessMethodInterceptor extends APIServiceConfigMethodInterceptor implements Ordered {
    
    public static final String DEFAULT_PARAMS_KEY = "_DEFAULT_PARAMS";
    
    @Autowired
    private ObjectProvider<List<DefaultRequestParameterProcessor>> objectProvider;
    
    @Override
    protected Object doInvoke(MethodInvocation invocation, APIServiceConfig serviceConfig) throws Throwable {
        AbstractRequestEntity<?> request = (AbstractRequestEntity<?>) APIServiceContext.getAttribute(invocation, REQUEST_ENTITY_KEY);
        
        Map<String, Object> defaultParams = new HashMap<>();
        objectProvider.ifAvailable(processors -> {
            processors.stream()
                      .filter(processor -> serviceConfig.getServiceType().equals(processor.getServiceType()))
                      .forEach(processor -> {
                          Map<String, Object> values = processor.getValues(request);
                          if (values != null) {
                              defaultParams.putAll(values);
                          }
                      });
        });
        
        if (!defaultParams.isEmpty()) {
            request.getParams().put(DEFAULT_PARAMS_KEY, defaultParams);
        }
        return invocation.proceed();
    }
    
    @Override
    public int getOrder() {
        return InterceptorType.FILL_DEFAULT_PARAMETER.getOrder();
    }
}
