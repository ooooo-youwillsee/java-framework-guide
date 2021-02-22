## 实现`componenetScan`这种类型的注解

测试说明：
1. 实现注解 `@XXXComponentScan`， 会扫描所有的 `@XXXService`
1. 启动 `com.ooooo.ComponentScanApplication` 类
2. `main` 方法中有测试调用类 `TestXXXService`
