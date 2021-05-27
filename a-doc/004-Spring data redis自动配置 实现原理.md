# spring-data-redis 实现原理


## 核心类

- `RedisAutoConfiguration`: 自动配置类，从 `@Import` 注解可以看出支持两种实现 `LettuceConnectionConfiguration` 和 `JedisConnectionConfiguration`, 推荐使用 `Lettuce` (也是 spring 默认支持)

- `StringRedisTemplate`: spring 默认提供的, 需要注意序列化方式 `RedisSerializer.string()`， 实际使用中，我们都会定制**使用json序列化**的 `redisTemplate`

- `RedisTemplate<Object, Object>`: spring 默认提供的，使用的 `jdk` 序列化，也就是说**实体类要实现 Serializable 接口**






## 注意

- 不同的系统之间，对**相同的key**使用**相同的序列化方式**
