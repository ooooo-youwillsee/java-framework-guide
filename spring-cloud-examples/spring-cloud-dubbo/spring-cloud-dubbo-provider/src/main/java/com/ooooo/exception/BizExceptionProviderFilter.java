package com.ooooo.exception;

import java.util.HashMap;
import java.util.Map;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

import static com.alibaba.fastjson.JSON.toJSONString;
import static com.ooooo.service.constant.CommonConstant.ERROR_INFO;
import static com.ooooo.service.constant.CommonConstant.ERROR_NO;
import static com.ooooo.service.constant.CommonConstant.EXCEPTION_MARKER;
import static org.apache.dubbo.common.constants.CommonConstants.PROVIDER;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/12/10 17:12
 * @since 1.0.0
 */
@Activate(group = {PROVIDER}, order = 100)
public class BizExceptionProviderFilter implements Filter, Filter.Listener {
	
	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		Result result = invoker.invoke(invocation);
		
		if (result.hasException()) {
			Throwable exception = result.getException();
			if (exception instanceof APIException) {
				APIException apiException = (APIException) exception;
				
				Map<String, String> exceptionMap = new HashMap<>();
				exceptionMap.put(ERROR_NO, apiException.getErrorNo());
				exceptionMap.put(ERROR_INFO, apiException.getErrorInfo());
				
				RuntimeException runtimeException = new RuntimeException(EXCEPTION_MARKER + toJSONString(exceptionMap));
				result.setException(runtimeException);
			}
		}
		return result;
	}
	
	@Override
	public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
	
	}
	
	@Override
	public void onError(Throwable t, Invoker<?> invoker, Invocation invocation) {
	
	}
}
