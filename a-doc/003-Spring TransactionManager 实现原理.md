# TransactionManager 实现原理


## 核心类

- `DataSourceTransactionManager`：数据源事务管理器，如 mysql

- `TransactionStatus`： 事务状态，默认实现类 `DefaultTransactionStatus`，其中 **transaction** 属性 ( 实现类 `DataSourceTransactionObject` ) 里面有 `connectionHolder` 属性，从中获取数据库连接 （在**开启事务**中会存入 `Connection`）

- `@Transactional`: 注解（隔离级别、事务传播类型，异常捕获类型）, 默认实现 `AnnotationTransactionAttributeSource`

- `TransactionSynchronizationManager`： 用于**绑定connection资源**和**TransactionSynchronization的回调**

- 对于事务，也是可以发布事件的 `TransactionalApplicationListener`，对应的注解 `@TransactionalEventListener`，这些都借助于 `TransactionSynchronizationManager`的 `TransactionSynchronization` 回调实现， 例如可以做审核操作后短信通知。


## 原理

- 开启事务： `org.springframework.transaction.PlatformTransactionManager#getTransaction`
  
- 提交事务： `org.springframework.transaction.PlatformTransactionManager#commit`


1. 利用 `ThreadLocal` 绑定到当前线程，源码位置在类 `DataSourceTransactionManager` 中 `TransactionSynchronizationManager.bindResource(obtainDataSource(), txObject.getConnectionHolder());` 和 `org.springframework.jdbc.datasource.DataSourceTransactionManager#doCleanupAfterCompletion()` 

2. `TransactionProxyFactoryBean`: 针对 xml 配置的事务管理器， 最终委托 `TransactionInterceptor` 来做事务管理。

3. `AnnotationTransactionAspect`: 注意这个是 `aspect` class , 对 `@transactional` 的表达式判断。



## spring 预留接口

1. commit 操作前：`org.springframework.transaction.support.AbstractPlatformTransactionManager#prepareForCommit`

2. 当前事务状态： `TransactionAspectSupport#currentTransactionStatus()` ， 通过方法 `setRollbackOnly()` 强制回滚事务
   
3. 当前事务信息： `TransactionAspectSupport#currentTransactionInfo()` 