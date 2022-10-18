package com.ooooo.exception;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.ooooo.service.constant.CommonConstant.ERROR_INFO;
import static com.ooooo.service.constant.CommonConstant.ERROR_NO;
import static com.ooooo.service.constant.CommonConstant.EXCEPTION_MARKER;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.apache.dubbo.common.constants.CommonConstants.CONSUMER;

import com.alibaba.fastjson.TypeReference;
import java.util.Map;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/12/10 17:05
 * @since 1.0.0
 */
@Activate(group = {CONSUMER}, order = -100)
public class BizExceptionConsumerFilter implements Filter, Filter.Listener {

	@Override
	public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
		Result result = invoker.invoke(invocation);

		if (result.hasException()) {
			Throwable exception = result.getException();
			String message = exception.getMessage();

			// transfrom to bizException
			if (isNotBlank(message) && message.contains(EXCEPTION_MARKER)) {
				String exceptionJson = message.substring(EXCEPTION_MARKER.length());
				Map<String, Object> exceptionMap = parseObject(exceptionJson, new TypeReference<Map<String, Object>>() {});

				String error_no = (String) exceptionMap.get(ERROR_NO);
				String error_info = (String) exceptionMap.get(ERROR_INFO);

				BizException bizException = new BizException(error_no, error_info);
				result.setException(bizException);
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
