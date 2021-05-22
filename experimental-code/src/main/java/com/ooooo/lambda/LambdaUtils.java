package com.ooooo.lambda;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import lombok.SneakyThrows;

/**
 * @author leizhijie
 * @since 2021/5/22 16:03 lamda 函数识别
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
