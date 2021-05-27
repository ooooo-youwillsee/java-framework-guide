# Spring Cloud 加载配置的实现原理


## 1、核心类:

- `PropertySourceBootstrapConfiguration`： 负责初始化bootstrap的配置加载, 具体调用方法 `initialize` 来实现配置文件的加载，比如 `spring cloud config`，所以对于**配置中心**的配置获取，只需要实现接口 `org.springframework.cloud.bootstrap.config.PropertySourceLocator`。

- `PropertySourceBootstrapProperties`：负责配置**属性覆盖策略**

- `BootstrapApplicationListener`：bootstrap 启动监听器，负责创建 `spring cloud` 的 `applicationContext`。

## 2、原理：

- 通过 `EventPublishingRunListener（spring boot的监听器）` 通过方法 `environmentPrepared()` 来发布事件 `ApplicationEnvironmentPreparedEvent`，然后这个 `BootstrapApplicationListener` 监听到了，通过其中的方法 `bootstrapServiceContext()` 来开始创建 **bootstrap** 的上下文，具体实现通过语句 `builder.sources(BootstrapImportSelectorConfiguration.class);` 指定了加载 `@BootstrapConfiguration` 的自动装配配置类，然后结合上面的 `PropertySourceBootstrapConfiguration` 类来完成。



