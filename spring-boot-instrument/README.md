```shell
cd java-framework-guide/spring-boot-instrument

# 打包 spring-boot-application
gradle bootJar -p spring-boot-application

# 打包 spring-boot-agent
gradle shadowJar -p spring-boot-agent 

# 正常模式运行
java -jar spring-boot-application/build/libs/spring-boot-application-1.0.0.jar

# 执行 agent 
com.ooooo.instrument.AgentLoader
```