package com.ooooo.lambda;

import java.io.Serializable;
import java.util.function.Function;

/**
 * @author leizhijie
 * @since 2021/5/22 16:33
 */
public interface SFunction<T, R> extends Function<T, R>, Serializable {
}
