package com.ooooo.auth.util.crypt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ooooo.auth.crypt.JwtTokenUtil;
import com.ooooo.auth.crypt.RSAKeyUtil;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author leizhijie
 * @date 2022/3/29 16:12
 * @since 1.0.0
 */
@Slf4j
class JwtUtilTest {

  private RSAPrivateKey privateKey;

  private RSAPublicKey publicKey;

  @BeforeEach
  public void init() {
    Pair<RSAPublicKey, RSAPrivateKey> pair = RSAKeyUtil.generateRSAKeyPair();
    publicKey = pair.getKey();
    privateKey = pair.getValue();
  }

  @Test
  void test() {
    Map<String, Object> header = new HashMap<>();
    header.put("111", "222");

    Map<String, Object> payload = new HashMap<>();
    payload.put("client_id", "123");

    String token = JwtTokenUtil.signToken(header, payload, privateKey);
    log.info("token: \n{}", token);

    DecodedJWT jwtToken = JwtTokenUtil.verifyToken(token, publicKey);
    assertEquals("222", jwtToken.getHeaderClaim("111").asString());
    assertEquals("123", jwtToken.getClaim("client_id").asString());
  }

}