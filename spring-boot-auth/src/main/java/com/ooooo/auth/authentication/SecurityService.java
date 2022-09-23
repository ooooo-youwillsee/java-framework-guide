package com.ooooo.auth.authentication;

import org.springframework.security.core.Authentication;

/**
 * @author leizhijie
 * @date 2022/3/29 15:14
 * @since 1.0.0
 */
public interface SecurityService {

  AccessToken resolve(Authentication authentication);

  Authentication resolve(String accessToken);
}
