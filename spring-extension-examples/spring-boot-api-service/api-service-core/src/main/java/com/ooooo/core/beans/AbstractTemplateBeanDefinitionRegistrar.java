package com.ooooo.core.beans;

import com.ooooo.core.constants.ServiceType;
import com.ooooo.core.request.ITemplate;
import com.ooooo.core.request.TemplateProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.context.support.GenericApplicationContext;

import java.util.HashMap;
import java.util.List;

/**
 * 抽象类， 负责注册template BeanDefinition
 *
 * @author leizhijie
 * @see ITemplate
 * @see TemplateProperties
 * @see SmartApplicationListener
 * @since 1.0.0
 */
public abstract class AbstractTemplateBeanDefinitionRegistrar implements InitializingBean, ApplicationContextAware {

	@Getter
	protected HashMap<String/* templateId */, TemplateProperties> registeredTemplatePropertiesMap = new HashMap<>();

	@Setter
	protected ApplicationContext applicationContext;

	@Override
	public void afterPropertiesSet() {
		registerTemplateBeans();
	}
	
	protected void registerTemplateBeans() {
		if (applicationContext instanceof GenericApplicationContext) {
			GenericApplicationContext context = (GenericApplicationContext) applicationContext;
			
			List<TemplateProperties> templatePropertiesList = getTemplatePropertiesList(context);
			for (TemplateProperties templateProperties : templatePropertiesList) {
				if (templateProperties.getId() == null) {
					throw new IllegalArgumentException("templateId没有配置");
				}
				if (registeredTemplatePropertiesMap.containsKey(templateProperties.getId())) {
					throw new IllegalArgumentException("templateId['" + templateProperties.getId() + "']配置重复");
				}
				registeredTemplatePropertiesMap.put(templateProperties.getId(), templateProperties);
				
				AnnotatedGenericBeanDefinition beanDefinition = getBeanDefintion(templateProperties);
				String beanName = getBeanName(templateProperties);
				context.registerBeanDefinition(beanName, beanDefinition);
			}
		}
	}
	
	protected abstract List<TemplateProperties> getTemplatePropertiesList(GenericApplicationContext context);
	
	protected String getBeanName(TemplateProperties templateProperties) {
		return templateProperties.getTemplateClassName() + "#" + templateProperties.getId();
	}
	
	public static String getTemplateId(String beanName) {
		if (beanName.contains("#")) {
			return beanName.split("#")[1];
		}
		return beanName;
	}
	
	protected AnnotatedGenericBeanDefinition getBeanDefintion(TemplateProperties templateProperties) {
		Class<?> templateClass = templateProperties.resolveTemplateClass();
		AnnotatedGenericBeanDefinition beanDefinition = new AnnotatedGenericBeanDefinition(templateClass);
		beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(templateProperties);
		return beanDefinition;
	}
	
	public abstract ServiceType getSerivceType();
}
