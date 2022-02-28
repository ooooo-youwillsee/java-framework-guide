package com.ooooo.yaml;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ooooo.yaml.YamlUtil.Address;
import com.ooooo.yaml.YamlUtil.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/2/28 10:05
 * @since 1.0.0
 */
@Slf4j
class YamlUtilTest {

  public static List<User> createUserList() {
    List<Address> addresses = new ArrayList<>();
    addresses.add(new Address("xx", "xxx"));
    addresses.add(new Address("yy", "yyy"));

    User user = new User();
    user.setId("1");
    user.setName("tom");
    user.setAddresses(addresses);

    return Collections.singletonList(user);
  }

  @Test
  void toYamlString() {
    List<User> userList = createUserList();
    String result = YamlUtil.toYamlString(userList);
    log.info("result:\n {}", result);
  }

  @Test
  void fromYamlString() {
    List<User> userList = createUserList();
    String result = YamlUtil.toYamlString(userList);

    userList = YamlUtil.fromYamlString(result);
    assertEquals(1, userList.size());

    User user = userList.get(0);
    assertEquals("1", user.getId());
    assertEquals("tom", user.getName());
    assertEquals("xx", user.getAddresses().get(0).getProvince());
    assertEquals("xxx", user.getAddresses().get(0).getCity());
  }
}