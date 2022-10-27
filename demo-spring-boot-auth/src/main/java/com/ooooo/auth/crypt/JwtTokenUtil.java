package com.ooooo.auth.crypt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * @author leizhijie
 * @date 2022/3/29 15:12
 * @since 1.0.0
 */
@Slf4j
public class JwtTokenUtil {

  public static final String ISSUER = "crh";

  public static String signToken(Map<String, Object> header, Map<String, ?> payload, RSAPrivateKey privateKey) {
    Algorithm algorithm = Algorithm.RSA256(null, privateKey);
    return JWT.create()
        .withHeader(header)
        .withPayload(payload)
        .withIssuer(ISSUER)
        .sign(algorithm);
  }

  public static DecodedJWT verifyToken(String token, RSAPublicKey publicKey) {
    try {
      Algorithm algorithm = Algorithm.RSA256(publicKey, null);
      JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
      return verifier.verify(token);

    } catch (JWTVerificationException exception) {
      log.error("jwtToken[{}] 解密失败, {}", token, exception);
      throw new IllegalArgumentException("解密失败, jwtToken['" + token + "']");
    }
  }


}
