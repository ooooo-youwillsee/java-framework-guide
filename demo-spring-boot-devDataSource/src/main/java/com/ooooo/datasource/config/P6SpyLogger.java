package com.ooooo.datasource.config;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/5 18:01
 * @since 1.0.0
 */
public class P6SpyLogger implements MessageFormattingStrategy {

  @Override
  public String formatMessage(int connectionId, String now, long elapsed, String category,
                              String prepared, String sql, String url) {
    return StringUtils.isNotBlank(sql) ? " Consume Time：" + elapsed + " ms " + now +
        "\n Execute SQL：" + sql.replaceAll("[\\s]+", " ") + "\n" : "";
  }
}