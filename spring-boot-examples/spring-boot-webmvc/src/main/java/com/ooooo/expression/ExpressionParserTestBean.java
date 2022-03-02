package com.ooooo.expression;

import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/2/14 16:42
 * @since 1.0.0
 */
@Component("expressionParserTestBean")
public class ExpressionParserTestBean {

    public String invoke(String x, String y) {
        return x + y;
    }

    public int invoke2(int xx, int yy) {
        return xx + yy;
    }
}