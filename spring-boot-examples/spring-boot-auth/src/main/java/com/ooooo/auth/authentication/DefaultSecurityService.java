package com.ooooo.auth.authentication;


import static com.ooooo.auth.authentication.AccessTokenAuthentication.AUTHORITIES;
import static com.ooooo.auth.authentication.AccessTokenAuthentication.DETAILS;
import static com.ooooo.auth.authentication.AccessTokenAuthentication.EXPIRES_AT;
import static com.ooooo.auth.authentication.AccessTokenAuthentication.NAME;
import static com.ooooo.auth.authentication.AccessTokenAuthentication.PRINCIPAL;
import static com.ooooo.auth.authentication.AccessTokenAuthentication.UNIQUE_ID;
import static com.ooooo.auth.authentication.AccessTokenAuthentication.getAuthorities;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ooooo.auth.config.SM4KeyPairConfig;
import com.ooooo.auth.crypt.JwtTokenUtil;
import com.ooooo.auth.crypt.SM4Utils;
import java.nio.charset.StandardCharsets;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

/**
 * @author leizhijie
 * @date 2022/3/29 15:28
 * @since 1.0.0
 */
public class DefaultSecurityService implements SecurityService {

  @Autowired
  private RSAPrivateKey privateKey;

  @Autowired
  private RSAPublicKey publicKey;

  @Autowired
  private SM4KeyPairConfig sm4KeyPairConfig;

  private final SM4Utils sm4Utils = new SM4Utils();

  @PostConstruct
  public void init() {
    sm4Utils.setHexString(sm4KeyPairConfig.isHexString());
  }

  @Override
  public AccessToken resolve(Authentication authentication) {
    if (!(authentication instanceof AccessTokenAuthentication)) {
      return AccessToken.INVALID_ACCESS_TOKEN;
    }
    AccessTokenAuthentication accessTokenAuthentication = (AccessTokenAuthentication) authentication;

    // payload
    Map<String, Object> payload = new HashMap<>();
    payload.put(PRINCIPAL, accessTokenAuthentication.getPrincipal());
    payload.put(DETAILS, accessTokenAuthentication.getDetails());
    payload.put(AUTHORITIES, getAuthorities(accessTokenAuthentication.getAuthorities()));
    payload.put(NAME, accessTokenAuthentication.getName());
    payload.put(UNIQUE_ID, accessTokenAuthentication.getUniqueId());
    payload.put(EXPIRES_AT, accessTokenAuthentication.getExpiresAt());

    // jwt token encode
    String accessToken = JwtTokenUtil.signToken(null, payload, privateKey);
    // SM4 encode
    accessToken = sm4Utils.encryptData_CBC("{jwt}" + accessToken, sm4KeyPairConfig.getSecretKey(), sm4KeyPairConfig.getIv());
    // build accessToken
    return new AccessToken(accessToken, accessTokenAuthentication.getExpiresAt());
  }

  @Override
  public Authentication resolve(String accessToken) {
    if (StringUtils.isBlank(accessToken)) {
      return null;
    }

    AccessTokenAuthentication authentication = getAuthentication(accessToken);
    authentication.setAuthenticated(authentication.getExpiresAt().after(new Date()));
    return authentication;
  }

  private AccessTokenAuthentication getAuthentication(String accessToken) {
    // parse prcefix, eg: accessToken: "{json}xxxxx"
    String prefix = "NONE";
    int lIndex = accessToken.indexOf("{"), rIndex = accessToken.indexOf("}");
    if (lIndex == 0 && rIndex > lIndex) {
      prefix = accessToken.substring(lIndex + 1, rIndex);
      accessToken = accessToken.substring(rIndex + 1);
    }

    switch (prefix) {
      case "base64":
        accessToken = new String(Base64.decodeBase64(accessToken), StandardCharsets.UTF_8);
        return getAuthentication(accessToken);
      case "json":
        return JSON.parseObject(accessToken, AccessTokenAuthentication.class);
      case "sm4":
        // SM4 decode
        String jwtToken = sm4Utils.decryptData_CBC(accessToken, sm4KeyPairConfig.getSecretKey(), sm4KeyPairConfig.getIv());
        return getAuthentication(jwtToken);
      case "jwt":
        // JWT decode
        DecodedJWT decodedJWT = JwtTokenUtil.verifyToken(accessToken, publicKey);
        // build AccessTokenAuthentication
        AccessTokenAuthentication authentication = new AccessTokenAuthentication();
        authentication.setPrincipal(decodedJWT.getClaim(PRINCIPAL).asMap());
        authentication.setDetails(decodedJWT.getClaim(DETAILS).asMap());
        authentication.setAuthorities(getAuthorities(decodedJWT.getClaim(AUTHORITIES).asList(String.class)));
        authentication.setName(decodedJWT.getClaim(NAME).asString());
        authentication.setUniqueId(decodedJWT.getClaim(UNIQUE_ID).asString());
        authentication.setExpiresAt(decodedJWT.getClaim(EXPIRES_AT).asDate());
        return authentication;
      default:
        return getAuthentication("{sm4}" + accessToken);
    }
  }


}
