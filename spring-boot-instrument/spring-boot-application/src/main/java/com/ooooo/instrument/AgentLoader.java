package com.ooooo.instrument;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class AgentLoader {

  private static final String TARGET_DISPLAY_NAME = "spring-boot-application";

  private static final String AGENT_JAR_FILE = "/Users/ooooo/Code/Self/java-framework-guide/spring-boot-instrument/spring-boot-agent/build/libs/spring-boot-agent-1.0.0-all.jar";

  public static void main(String[] args) {
    Optional<VirtualMachineDescriptor> vmOpt = VirtualMachine.list()
        .stream()
        .filter(v -> v.displayName().contains(TARGET_DISPLAY_NAME))
        .findFirst();

    if (!vmOpt.isPresent()) {
      log.error("{} isn't found.", TARGET_DISPLAY_NAME);
      return;
    }

    String vmId = vmOpt.get().id();

    VirtualMachine vm;
    try {
      vm = VirtualMachine.attach(vmId);
      vm.loadAgent(AGENT_JAR_FILE, "com.sun.management.jmxremote.port=5000");
      vm.detach();
    } catch (Throwable e) {
      log.error("open vmId[{}]", vmId, e);
    }
  }
}
