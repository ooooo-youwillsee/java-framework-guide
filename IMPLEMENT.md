# 已实现的功能列表

## 1. spring-mvc-argument-resolver

1. 实现了注解 `@XRequestBody` 注解，可以接受如下的复杂结构：

```
{
  "body": { "user": { "id": "1", "name": "tom" } },
  "header": { "requestId": "123456", "timeStamp": 1621668184519 }
}

```

## 2. spring-mvc-requestmapping

1. 实现了注解 `@XRequestMapping` 注解，可以接受请求

## 3. spring-redis

1. spring redis 基本使用
2. spring redis repository 基本使用
3. redisson 基本使用

### 4. spring-simple-listener

1. 实现了注解 `@SimpleListener` 接口，可以接受事件监听

### 5. netty

1. 简单测试 netty 内置的 **LineBasedFrameDecoder**， **FixedLengthFrameDecoder**， **LengthFieldBasedFrameDecoder**

### 6. gateway-security

1. 实现oauth2.0 的授权码模式
2. spring cloud gateway 添加 redislimiter

### 7. component-scan

1. 实现了注解 `@APIServiceComponentScan`

### 8. experimental-code

1. `LambdaUtils` 类解析 lambda 函数的属性

## 9. spring-boot-class-loader

实现一个**自定义的classLoader**, 来解决jar包版本冲突问题

`module-a` 和 `module-b` 来模拟两个jar，这两个jar包中的类，都会在` module-main` 中调用

## 10. spring-boot-compositePropertySources

实现一个`统一获取配置`的类: `compositePropertySources`

## 11. spring-boot-lambda

lambda 测试

## 12. spring-boot-caffeine

caffeine 测试
