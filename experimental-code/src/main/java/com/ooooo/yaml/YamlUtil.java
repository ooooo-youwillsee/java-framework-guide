package com.ooooo.yaml;

import com.alibaba.fastjson.JSON;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/2/28 09:32
 * @since 1.0.0
 */
public class YamlUtil {

  private static final Yaml yaml = new Yaml();

  static {
    yaml.addTypeDescription(new TypeDescription(User.class, Tag.MAP));
    yaml.addTypeDescription(new TypeDescription(Address.class, Tag.MAP));
  }

  public static String toYamlString(List<User> userList) {
    return yaml.dumpAsMap(Collections.singletonMap("users", userList));
  }

  public static List<User> fromYamlString(String usersString) {
    Object list = yaml.loadAs(usersString, Map.class).get("users");
    return JSON.parseArray(JSON.toJSONString(list), User.class);
  }


  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class User {

    private String id;
    private String name;
    private List<Address> addresses;
  }


  @Data
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Address {

    private String province;
    private String city;
  }
}
