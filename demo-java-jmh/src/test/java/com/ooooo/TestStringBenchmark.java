package com.ooooo;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
// 预热的参数
@Warmup(time = 1)
// 测试的参数
@Measurement(time = 1)
// 可以添加 JVM 参数来测试
@Fork(value = 1)
@State(Scope.Thread)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class TestStringBenchmark {

  @Benchmark
  public String stringBuilder() {
    StringBuilder sb = new StringBuilder();
    sb.append("hello");
    sb.append("world");
    return sb.toString();
  }


  @Benchmark
  public String stringBuffer() {
    StringBuffer sb = new StringBuffer();
    sb.append("hello");
    sb.append("world");
    return sb.toString();
  }

  @Benchmark
  public String stringConcat() {
    return "hello" + "world";
  }

}
