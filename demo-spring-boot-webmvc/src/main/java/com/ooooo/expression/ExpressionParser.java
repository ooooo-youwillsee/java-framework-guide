package com.ooooo.expression;

import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/1/25 16:49
 * @since 1.0.0
 */
public interface ExpressionParser {

    String EXPRESSION = "expression";

    <T> T parse(Map<String, Object> params, String expression, Class<T> returnClazz);

}
