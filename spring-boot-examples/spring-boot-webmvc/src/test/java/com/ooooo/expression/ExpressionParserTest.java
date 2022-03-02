package com.ooooo.expression;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/03/02 21:09
 * @since 1.0.0
 */
@SpringBootTest
class ExpressionParserTest {

    @Autowired
    private ExpressionParser expressionParser;

    @Test
    void parse1() {
        Map<String, Object> params = new HashMap<>();
        params.put("x", "1");
        params.put("y", "2");

        String expression = "#{ x + y}";
        String result = expressionParser.parse(params, expression, String.class);
        assertEquals("12", result);
    }


    @Test
    void parse2() {
        Map<String, Object> params = new HashMap<>();
        params.put("x", "1");
        params.put("y", "2");

        String expression = "#{ @expressionParserTestBean.invoke(#x, #y) }";
        String result = expressionParser.parse(params, expression, String.class);
        assertEquals("12", result);
    }


    @Test
    void parse3() {
        Map<String, Object> params = new HashMap<>();
        params.put("xx", 1);
        params.put("yy", 2);

        String expression = "#{ @expressionParserTestBean.invoke2(#xx, #yy) }";
        Integer result = expressionParser.parse(params, expression, Integer.class);
        assertEquals(3, result);
    }

}