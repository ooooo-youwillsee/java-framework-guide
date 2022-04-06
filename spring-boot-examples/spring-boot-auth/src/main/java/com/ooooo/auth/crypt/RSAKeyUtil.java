package com.ooooo.auth.crypt;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

/**
 * @author leizhijie
 * @date 2022/3/30 13:47
 * @since 1.0.0
 */
@Slf4j
public class RSAKeyUtil {

  public static Pair<RSAPublicKey, RSAPrivateKey> generateRSAKeyPair() {
    KeyPairGenerator keyPairGen = null;
    try {
      keyPairGen = KeyPairGenerator.getInstance("RSA");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    // 初始化密钥对生成器，密钥大小为96-1024位
    keyPairGen.initialize(1024, new SecureRandom());
    KeyPair keyPair = keyPairGen.generateKeyPair();
    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
    return Pair.of(publicKey, privateKey);
  }

  public static Triple<BigInteger, BigInteger, BigInteger> generateRSAKeyTriple() {
    Pair<RSAPublicKey, RSAPrivateKey> keyPair = generateRSAKeyPair();
    RSAPublicKey publicKey = keyPair.getKey();
    RSAPrivateKey privateKey = keyPair.getValue();

    BigInteger publicExponent = publicKey.getPublicExponent();
    BigInteger privateExponent = privateKey.getPrivateExponent();
    BigInteger modulus = privateKey.getModulus();

    log.info("publicExponent: \n{}", publicExponent);
    log.info("privateExponent: \n{}", privateExponent);
    log.info("modulus: \n{}", modulus);
    return Triple.of(publicExponent, privateExponent, modulus);
  }


  @SneakyThrows
  public static RSAPrivateKey rsaPrivateKey(BigInteger modulus, BigInteger privateExponent) {
    RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(modulus, privateExponent);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PrivateKey privKey = fact.generatePrivate(keySpec);
    return (RSAPrivateKey) privKey;
  }


  @SneakyThrows
  public static RSAPublicKey rsaPublicKey(BigInteger modulus, BigInteger publicExponent) {
    RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
    KeyFactory fact = KeyFactory.getInstance("RSA");
    PublicKey pubKey = fact.generatePublic(keySpec);
    return (RSAPublicKey) pubKey;
  }

}
