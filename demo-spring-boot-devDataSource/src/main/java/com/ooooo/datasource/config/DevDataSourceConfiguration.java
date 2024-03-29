package com.ooooo.datasource.config;

import static java.lang.Boolean.parseBoolean;
import static org.springframework.util.ReflectionUtils.findField;
import static org.springframework.util.ReflectionUtils.getField;
import static org.springframework.util.ReflectionUtils.makeAccessible;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.lang.reflect.Field;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/5 18:01
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class DevDataSourceConfiguration implements BeanPostProcessor, EnvironmentAware {

  @Setter
  private Environment environment;

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof DataSource && parseBoolean(environment.resolvePlaceholders("${dev.sql-log.enabled:false}"))) {
      log.info("已开启日志打印，将使用[P6SpyDriver]");
      if (environment.acceptsProfiles(Profiles.of("run"))) {
        log.warn("在生产环境一定要关闭配置[dev.sql-log.enabled]");
      }
      return proxyDataSource((DataSource) bean);
    }
    return bean;
  }

  public DataSource proxyDataSource(DataSource dataSource) {
    if (dataSource instanceof AbstractRoutingDataSource) {
      AbstractRoutingDataSource abstractRoutingDataSource = (AbstractRoutingDataSource) dataSource;

      // resolvedDataSources
      Field resolvedDataSourcesField = findField(AbstractRoutingDataSource.class, "resolvedDataSources");
      makeAccessible(resolvedDataSourcesField);

      @SuppressWarnings("unchecked")
      Map<Object, DataSource> resolvedDataSources = (Map<Object, DataSource>) getField(resolvedDataSourcesField, abstractRoutingDataSource);
      if (resolvedDataSources != null) {
        resolvedDataSources.forEach((k, v) -> resolvedDataSources.put(k, convertToProxyDataSource(v)));
      }

      // resolvedDefaultDataSource
      Field resolvedDefaultDataSourceField = findField(AbstractRoutingDataSource.class, "resolvedDefaultDataSource");
      makeAccessible(resolvedDefaultDataSourceField);

      DataSource resolvedDefaultDataSource = (DataSource) getField(resolvedDefaultDataSourceField, abstractRoutingDataSource);
      if (resolvedDefaultDataSource != null) {
        ReflectionUtils.setField(resolvedDefaultDataSourceField, abstractRoutingDataSource, convertToProxyDataSource(resolvedDefaultDataSource));
      }

      return abstractRoutingDataSource;
    }

    return convertToProxyDataSource(dataSource);
  }


  public DataSource convertToProxyDataSource(DataSource dataSource) {
    if (dataSource instanceof HikariDataSource) {
      HikariConfig oldConfig = (HikariDataSource) dataSource;

      // jdbc:h2:mem:test to  jdbc:p6spy:h2:mem:test
      String jdbcUrl = oldConfig.getJdbcUrl();
      if (!jdbcUrl.contains("p6spy")) {
        jdbcUrl = "jdbc:p6spy" + jdbcUrl.substring(4);
      }

      HikariConfig newConfig = new HikariConfig();
      newConfig.setPoolName("proxy-P6SpyDriver");
      newConfig.setDriverClassName("com.p6spy.engine.spy.P6SpyDriver");
      newConfig.setJdbcUrl(jdbcUrl);
      newConfig.setUsername(oldConfig.getUsername());
      newConfig.setPassword(oldConfig.getPassword());
      return new HikariDataSource(newConfig);
    }
    return dataSource;
  }

}
