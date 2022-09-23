package com.ooooo.auth.convert;

/**
 * 转换类
 *
 * @author leizhijie
 * @since 1.0.0
 */
public interface ConverterApplyFactory {


    /**
     * 将source对象转换为targetType类型
     *
     * @param source
     * @param targetType
     * @param <T>
     * @return
     */
    <T> T convert(Object source, Object targetType);

    <T> T convert(Object source, Object targetType, boolean useDefault);

    boolean canConvert(Class<?> sourceType, Class<?> targetType);

}
