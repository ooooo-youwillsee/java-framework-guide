package com.ooooo.auth.authentication;

import com.ooooo.auth.test.AuthTestController.Result;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leizhijie
 * @date 2022/3/30 16:31
 * @since 1.0.0
 */
@Slf4j
@RestController
public class AuthorizationEndpoint {

  public static final String TOKEN_URL = "/authless/token";

  @Autowired
  private SecurityService securityService;

  @Autowired(required = false)
  private ObjectProvider<List<AuthService>> authServices;

  @PostMapping(TOKEN_URL)
  public Result<AccessToken> token(HttpServletRequest request, @RequestBody Map<String, Object> params) {
    Authentication authentication = getAuthentication(request, params);
    return Result.success(securityService.resolve(authentication));
  }

  private Authentication getAuthentication(HttpServletRequest request, Map<String, Object> params) {
    List<AuthService> authServices = this.authServices.getIfAvailable();
    if (authServices == null || authServices.size() == 0) {
      throw new IllegalArgumentException("AuthService is null");
    }

    Authentication authentication = null;
    for (AuthService authService : authServices) {
      authentication = authService.auth(request, params);
      if (authentication != null) {
        break;
      }
    }
    if (authentication == null) {
      throw new IllegalArgumentException("not found suitable AuthService");
    }
    return authentication;
  }


  @PostConstruct
  public void init() {
    authServices.ifAvailable(s -> s.sort(AnnotationAwareOrderComparator.INSTANCE));
  }
}
