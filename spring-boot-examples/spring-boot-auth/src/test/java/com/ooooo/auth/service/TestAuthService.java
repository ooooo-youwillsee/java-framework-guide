package com.ooooo.auth.service;

import com.ooooo.auth.authentication.AccessTokenAuthentication;
import com.ooooo.auth.authentication.AuthService;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * @author leizhijie
 * @date 2022/3/30 17:31
 * @since 1.0.0
 */
@Component
public class TestAuthService implements AuthService {

  @Override
  public Authentication auth(HttpServletRequest request, Map<String, Object> params) {
    String username = (String) params.get("username");
    String password = (String) params.get("password");

    Map<String, Object> userInfo = new HashMap<>();
    userInfo.put("client_id", "123213");
    userInfo.put("client_name", "哈哈");

    String user_id = "123", device_flag = "1";
    AccessTokenAuthentication authentication = new AccessTokenAuthentication();
    authentication.setUniqueId(String.format("%s:%s", user_id, device_flag));
    authentication.setPrincipal(userInfo);
    authentication.setDetails(Collections.singletonMap("username", "tom"));
    authentication.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
    authentication.setExpiresAt(DateUtils.addHours(new Date(), 1));

    if ("tom".equals(username) && "123".equals(password)) {
      authentication.setAuthenticated(true);
    } else {
      authentication.setAuthenticated(false);
    }

    return authentication;
  }
}
