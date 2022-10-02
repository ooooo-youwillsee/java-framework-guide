```shell
cd java-framework-guide/spring-boot-instrument

# 打包 spring-boot-application
gradle bootJar -p spring-boot-application

# 打包 spring-boot-agent
gradle shadowJar -p spring-boot-agent 

# 正常模式运行
java -jar spring-boot-application/build/libs/spring-boot-application-1.0.0.jar

# 第一种方式：执行 agentLoader 
com.ooooo.instrument.AgentLoader

# 第二种方式，jvm 参数
-javaagent:/Users/ooooo/Code/Self/java-framework-guide/spring-boot-instrument/spring-boot-agent/build/libs/spring-boot-agent-1.0.0-all.jar
```