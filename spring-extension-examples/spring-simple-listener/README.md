## 实现 `@simpleListener` 注解

说明：
1. `SimpleMulticasterAwareBeanPostProcessor` 用来设置 `SimpleMulticaster`
2. `SimpleListenerAdapter` 是对有 `@simpleListener` 的方法一个简单适配器
3. 类型判断使用 Spring 中内置的 `ResolvableType`


特别注意：
1. `BeanPostProcessor` 中实现 `Ordered` 接口, 这个排序是初始化其他bean的 `BeanPostProcessor` 的顺序，其中不包含 `BeanPostProcessor` 的 bean。 关键源码位置： `org.springframework.context.support.PostProcessorRegistrationDelegate#registerBeanPostProcessors`
2. 基于上述的顺序说明，`com.ooooo.annotation.SimpleMulticasterAwareBeanPostProcessor` 要实现 `PriorityOrdered` 接口

测试说明：
1. 运行测试类 `com.ooooo.listenter.EventListenerTests`
