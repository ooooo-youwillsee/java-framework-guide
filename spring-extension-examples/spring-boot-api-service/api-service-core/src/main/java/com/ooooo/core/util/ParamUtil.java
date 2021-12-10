package com.ooooo.core.util;

import com.alibaba.fastjson.JSON;
import com.ooooo.core.request.IRequest;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.core.io.InputStreamSource;
import org.springframework.util.ReflectionUtils;

import static org.springframework.util.ClassUtils.isPrimitiveArray;
import static org.springframework.util.ClassUtils.isPrimitiveOrWrapper;
import static org.springframework.util.ClassUtils.isPrimitiveWrapperArray;
import static org.springframework.util.ReflectionUtils.getField;

/**
 * @author leizj
 * @date 2021/12/8 10:37
 * @since 1.0.0
 */
public class ParamUtil {
	
	public static String toJSONString(Object obj) {
		if (obj == null) return "";
		Object[] args = obj.getClass().isArray() ? (Object[]) obj : new Object[]{obj};
		
		Map<String, Object> jsonMap = new HashMap<>();
		Map<String, Object> map = toMap(args);
		for (Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (isMultiPartRecursively(value)) {
				jsonMap.put(key, "multipart type");
			} else {
				jsonMap.put(key, value);
			}
		}
		return JSON.toJSONString(jsonMap);
	}
	
	public static Map<String, Object> toMap(Object[] args) {
		if (args == null || args.length == 0) return new HashMap<>();
		
		Map<String, Object> params = new HashMap<>();
		for (Object arg : args) {
			if (arg == null) {
				continue;
			}
			
			if (arg instanceof IRequest) {
				params.putAll(((IRequest) arg).getParams());
			} else if (arg instanceof Map) {
				params.putAll((Map<String, ?>) arg);
			} else {
				ReflectionUtils.doWithFields(arg.getClass(), field -> {
					ReflectionUtils.makeAccessible(field);
					Object fieldValue = getField(field, arg);
					params.put(field.getName(), fieldValue);
				});
			}
		}
		return params;
	}
	
	public static boolean isMultiPartRecursively(Object value) {
		return isMultiPartRecursively(value, 10);
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isMultiPartRecursively(Object value, int depth) {
		if (value == null || depth <= 0) {
			return false;
		}
		if (isMultiPart(value)) {
			return true;
		}
		Class<?> clazz = value.getClass();
		if (clazz == Object.class || clazz == LocalDate.class || isPrimitiveOrWrapper(clazz) || isPrimitiveArray(clazz) || isPrimitiveWrapperArray(clazz)) {
			return false;
		}
		
		if (value instanceof List) {
			for (Object obj : (List) value) {
				if (isMultiPartRecursively(obj, --depth)) {
					return true;
				}
			}
		}
		if (value instanceof Map) {
			for (Object obj : ((Map) value).values()) {
				if (isMultiPartRecursively(obj, -depth)) {
					return true;
				}
			}
		}
		
		if (clazz.isArray()) {
			for (Object obj : (Object[]) value) {
				if (isMultiPartRecursively(obj, -depth)) {
					return true;
				}
			}
		}
		
		// use reflect
		Field[] fields = clazz.getFields();
		for (Field field : fields) {
			ReflectionUtils.makeAccessible(field);
			Object fieldValue = getField(field, value);
			if (isMultiPartRecursively(fieldValue, --depth)) {
				return true;
			}
		}
		
		return false;
	}
	
	public static boolean isMultiPart(Object value) {
		return value instanceof File || value instanceof InputStream || value instanceof InputStreamSource;
	}
	
}
