package com.ooooo.auth.util.crypt;

import com.ooooo.auth.crypt.RSAKeyUtil;
import org.junit.jupiter.api.Test;

/**
 * @author leizhijie
 * @date 2022/3/30 14:27
 * @since 1.0.0
 */
class RSAKeyUtilTest {

  @Test
  void generateRSAKeyPair() {
    RSAKeyUtil.generateRSAKeyPair();
    RSAKeyUtil.generateRSAKeyTriple();
  }
}