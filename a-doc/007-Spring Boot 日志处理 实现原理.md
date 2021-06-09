# Spring Boot日志处理 

## 1. 关键类

`org.springframework.boot.context.logging.LoggingApplicationListener`: 日志处理监听器
`org.springframework.boot.logging.LoggingSystem`: 日志抽象类
`org.springframework.boot.logging.logback.SpringBootJoranConfigurator`: Spring 对 logback 的扩展， 参考 [spring boot 扩展](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.logging.logback-extensions)


## 2. 源码分析， 以 **logback** 为例

`org.springframework.boot.logging.logback.LogbackLoggingSystem.getStandardConfigLocations`: 获取 logback 标准的配置文件
`org.springframework.boot.logging.AbstractLoggingSystem.getSpringConfigLocations`: Spring 自定义的配置文件
·

总结一句话就是：根据不同的事件来设置日志


