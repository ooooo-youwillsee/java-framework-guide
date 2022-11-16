# 执行测试类

由于使用 jdk17, 所以执行时，需要添加下列 jvm 参数：

```shell
--add-exports java.base/jdk.internal.misc=ALL-UNNAMED
```