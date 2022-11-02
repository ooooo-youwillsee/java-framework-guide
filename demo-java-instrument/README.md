```shell
cd java-framework-guide/demo-java-instrument

# 打包 demo-springApplication
gradle bootJar -p demo-springApplication

# 打包 demo-java-agent
gradle shadowJar -p demo-java-agent 

# 正常模式运行
java -jar demo-springApplication/build/libs/demo-springApplication-1.0.0.jar

# 第一种方式：执行 agentLoader 
com.ooooo.instrument.AgentLoader

# 第二种方式，jvm 参数
-javaagent:/Users/ooooo/Code/Self/java-framework-guide/demo-java-instrument/demo-java-agent/build/libs/demo-java-agent-1.0.0-all.jar
```