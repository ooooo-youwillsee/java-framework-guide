# Mybatis 插件机制 实现原理

版本： 3.5.6

## 1. 核心类

1. `org.apache.ibatis.plugin.Plugin`: 此类实现了 **InvocationHandler** 接口，用于动态代理

2. `org.apache.ibatis.plugin.InterceptorChain`: 对所有的 **interceptor** 进行包装

3. `org.apache.ibatis.plugin.Interceptor`: 拦截器的接口，实际使用中我们需要实现这个接口

4. `org.apache.ibatis.plugin.Intercepts`: 对拦截的方法进行声明

## 2. 用法

```java

@Intercepts({@Signature(
				type = Executor.class,
				method = "update",
				args = {MappedStatement.class, Object.class})})
public class ExamplePlugin implements Interceptor {
	private Properties properties = new Properties();

	public Object intercept(Invocation invocation) throws Throwable {
		// implement pre processing if need
		Object returnObject = invocation.proceed();
		// implement post processing if need
		return returnObject;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
}
```

## 3. 源码解析

1. 在 `org.apache.ibatis.session.Configuration` 中，调用 **interceptorChain.pluginAll ** 对四种类型的拦截器进行包装。例如 `interceptorChain.pluginAll(executor)`

2. 方法 `org.apache.ibatis.plugin.InterceptorChain#pluginAll`: 遍历所有的 **Interceptor** 实例，都调用 **interceptor.plugin(target)** ，每一个这个方法就会返回一个 proxy 对象，把这个proxy对象赋值给target，在调用 **interceptor.plugin(target)** 方法在产生出新的 proxy 对象。

3. 下一步，委托方法 `Plugin.wrap(target, this)` 来生成 proxy 对象， 先根据 Interceptor 实例获取声明的拦截方法 (@Intercepts 注解声明), `signatureMap` 的 key 就是类， 比如 `Executor.class`, 对应的 value 就是你要拦截的方法, 例如 update 。

4. 参数中 target 对象就是你先前包装的对象，例如 executor。

5. 方法 `getAllInterfaces` 获取所有的拦截的接口，如果大于0， 就调用 `Proxy.newProxyInstance()` 来生成代理对象

6. 代理对象执行方法时,会执行 `org.apache.ibatis.plugin.Plugin#invoke`, 如果方法匹配，进而执行 `org.apache.ibatis.plugin.Interceptor#intercept`, 这时就会执行你自己的逻辑， 你代码中调用 **invocation.proceed()**, 执行上一个代理对象的 intercept 方法。

