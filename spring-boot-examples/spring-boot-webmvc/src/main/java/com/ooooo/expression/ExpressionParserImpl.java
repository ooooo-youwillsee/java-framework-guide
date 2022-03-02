package com.ooooo.expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.expression.spel.support.StandardTypeLocator;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/2/14 16:42
 * @since 1.0.0
 */
@Service
public class ExpressionParserImpl implements ExpressionParser {

    @Autowired
    private ApplicationContext applicationContext;

    private volatile StandardEvaluationContext standardEvaluationContext;

    private final SpelExpressionParser parser = new SpelExpressionParser(new SpelParserConfiguration(true, true));
    private final ParserContext parserContext = new TemplateParserContext();


    @Override
    public <T> T parse(Map<String, Object> params, String expression, Class<T> returnClazz) {
        Expression parseExpression = parser.parseExpression(expression, parserContext);
        EvaluationContext evaluationContext = getOrCreateEvaluationContext();
        setVariables(evaluationContext, params);
        return parseExpression.getValue(evaluationContext, params, returnClazz);
    }

    private void setVariables(EvaluationContext evaluationContext, Map<String, Object> params) {
        params.forEach(evaluationContext::setVariable);
        evaluationContext.setVariable("params", params);
    }

    private EvaluationContext getOrCreateEvaluationContext() {
        if (standardEvaluationContext == null) {
            synchronized (ExpressionParser.class) {
                if (standardEvaluationContext == null) {
                    standardEvaluationContext = new StandardEvaluationContext(applicationContext.getAutowireCapableBeanFactory());
                    standardEvaluationContext.addPropertyAccessor(new BeanExpressionContextAccessor());
                    standardEvaluationContext.addPropertyAccessor(new BeanFactoryAccessor());
                    standardEvaluationContext.addPropertyAccessor(new MapAccessor());
                    standardEvaluationContext.addPropertyAccessor(new EnvironmentAccessor());
                    standardEvaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
                    standardEvaluationContext.setTypeLocator(new StandardTypeLocator(((ConfigurableBeanFactory) applicationContext.getAutowireCapableBeanFactory()).getBeanClassLoader()));
                    ConversionService conversionService = ((ConfigurableBeanFactory) applicationContext.getAutowireCapableBeanFactory()).getConversionService();
                    if (conversionService != null) {
                        standardEvaluationContext.setTypeConverter(new StandardTypeConverter(conversionService));
                    }
                }
            }
        }
        return standardEvaluationContext;
    }


}
