package com.ooooo.auth.authentication;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

/**
 * @author leizhijie
 * @date 2022/3/30 16:48
 * @since 1.0.0
 */
public interface AuthService {

  Authentication auth(HttpServletRequest request, Map<String, Object> params);
}
