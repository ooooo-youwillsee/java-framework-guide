package com.ooooo.lambda;

import lombok.SneakyThrows;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/5/22 16:03 lambda 函数识别
 */
public class LambdaUtils {

	// 正常情况，要做缓存
	public static <T> String resolveMethod(SFunction<T, ?> func) {
		SerializedLambda serializedLambda = resovle(func);
		String methodName = serializedLambda.getImplMethodName();
		return methodName;
	}

	@SneakyThrows
	public static SerializedLambda resovle(SFunction<?, ?> func) {
		Method method = func.getClass().getDeclaredMethod("writeReplace");
		method.setAccessible(true);
		return (SerializedLambda) method.invoke(func);
	}

}
