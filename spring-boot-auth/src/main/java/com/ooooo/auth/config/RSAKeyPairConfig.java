package com.ooooo.auth.config;

import com.ooooo.auth.crypt.RSAKeyUtil;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author leizhijie
 * @date 2022/3/29 17:15
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class RSAKeyPairConfig {

  private static BigInteger modulus = new BigInteger(
      "97265808473506708274508576055012712726882025675931268646867804479695323480704423484990873620366857843675569044650993039441233617938481451705700378766617189789532291478792514566859232689505963160490911918172695616726515907988063063873973601774866346604182511027432286273488012274404410960021961655642078082283");

  private static BigInteger publicExponent = new BigInteger(
      "65537");

  private static BigInteger privateExponent = new BigInteger(
      "60465176969027218240257471307158901524419802677313819917634942434155777254487982623586877369537301912037266863727758478720040569883144984114957033909558724232547084041456146157273800474436091820456396422465902648924179706079780878308555710333273859240112765136482334353929205496632836924518967298566722906297");

  @Bean
  public RSAPublicKey rsaPublicKey() {
    return RSAKeyUtil.rsaPublicKey(modulus, publicExponent);
  }

  @Bean
  public RSAPrivateKey rsaPrivateKey() {
    return RSAKeyUtil.rsaPrivateKey(modulus, privateExponent);
  }

}
