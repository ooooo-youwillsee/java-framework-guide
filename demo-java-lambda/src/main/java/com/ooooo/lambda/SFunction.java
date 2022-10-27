package com.ooooo.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/5/22 16:33
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
