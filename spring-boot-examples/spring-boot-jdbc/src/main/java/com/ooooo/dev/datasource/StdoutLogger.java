package com.ooooo.dev.datasource;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/5 18:01
 * @since 1.0.0
 */
public class StdoutLogger extends com.p6spy.engine.spy.appender.StdoutLogger {
	
	@Override
	public void logText(String text) {
		// 打印红色 SQL 日志
		System.err.println(text);
	}
}