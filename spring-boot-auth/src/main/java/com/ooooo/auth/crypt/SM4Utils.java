package com.ooooo.auth.crypt;


import com.ooooo.auth.crypt.SM4.SM4_Context;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Setter;
import org.apache.commons.lang3.RandomUtils;
import org.apache.tomcat.util.codec.binary.Base64;

@SuppressWarnings("DuplicatedCode")
public class SM4Utils {

  @Setter
  public boolean hexString = true;

  public SM4Utils() {
  }


  public String encryptData_ECB(String plainText, String secretKey) {
    try {
      SM4_Context ctx = new SM4_Context();
      ctx.isPadding = true;
      ctx.mode = SM4.SM4_ENCRYPT;

      byte[] keyBytes;
      if (hexString) {
        keyBytes = hexStringToBytes(secretKey);
      } else {
        keyBytes = secretKey.getBytes();
      }

      SM4 sm4 = new SM4();
      sm4.sm4_setkey_enc(ctx, keyBytes);
      byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes(StandardCharsets.UTF_8));
      return byteToHex(encrypted);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String decryptData_ECB(String cipherText, String secretKey) {
    try {
      byte[] encrypted = hexToByte(cipherText);
      cipherText = Base64.encodeBase64String(encrypted);
      //cipherText = new BASE64Encoder().encode(encrypted);
      if (cipherText != null && cipherText.trim().length() > 0) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(cipherText);
        cipherText = m.replaceAll("");
      }

      SM4_Context ctx = new SM4_Context();
      ctx.isPadding = true;
      ctx.mode = SM4.SM4_DECRYPT;

      byte[] keyBytes;
      if (hexString) {
        keyBytes = hexStringToBytes(secretKey);
      } else {
        keyBytes = secretKey.getBytes();
      }

      SM4 sm4 = new SM4();
      sm4.sm4_setkey_dec(ctx, keyBytes);
      byte[] decrypted = sm4.sm4_crypt_ecb(ctx, Base64.decodeBase64(cipherText));
      //byte[] decrypted = sm4.sm4_crypt_ecb(ctx, new BASE64Decoder().decodeBuffer(cipherText));
      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String encryptData_CBC(String plainText, String secretKey, String iv) {
    try {
      SM4_Context ctx = new SM4_Context();
      ctx.isPadding = true;
      ctx.mode = SM4.SM4_ENCRYPT;

      byte[] keyBytes;
      byte[] ivBytes;
      if (hexString) {
        keyBytes = hexStringToBytes(secretKey);
        ivBytes = hexStringToBytes(iv);
      } else {
        keyBytes = secretKey.getBytes();
        ivBytes = iv.getBytes();
      }

      SM4 sm4 = new SM4();
      sm4.sm4_setkey_enc(ctx, keyBytes);
      byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes(StandardCharsets.UTF_8));
      return byteToHex(encrypted);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String decryptData_CBC(String cipherText, String secretKey, String iv) {
    try {
      byte[] encrypted = hexToByte(cipherText);
      cipherText = Base64.encodeBase64String(encrypted);
      //cipherText = new BASE64Encoder().encode(encrypted);
      if (cipherText != null && cipherText.trim().length() > 0) {
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(cipherText);
        cipherText = m.replaceAll("");
      }
      SM4_Context ctx = new SM4_Context();
      ctx.isPadding = true;
      ctx.mode = SM4.SM4_DECRYPT;

      byte[] keyBytes;
      byte[] ivBytes;
      if (hexString) {
        keyBytes = hexStringToBytes(secretKey);
        ivBytes = hexStringToBytes(iv);
      } else {
        keyBytes = secretKey.getBytes();
        ivBytes = iv.getBytes();
      }

      SM4 sm4 = new SM4();
      sm4.sm4_setkey_dec(ctx, keyBytes);
      //byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, new BASE64Decoder().decodeBuffer(cipherText));
      byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, Base64.decodeBase64(cipherText));
      return new String(decrypted, StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public static void main(String[] args) {
    String plainText = "I Love You Every Day";
    String s = byteToHex(plainText.getBytes());
    System.out.println("原文" + s);
    System.out.println();

    SM4Utils sm4 = new SM4Utils();
    String secretKey = byteToHex(RandomUtils.nextBytes(16));
    String iv = byteToHex(RandomUtils.nextBytes(16));
    System.out.println("secretKey: " + secretKey);
    System.out.println("iv: " + iv);

    System.out.println("ECB模式加密");
    String cipherText = sm4.encryptData_ECB(plainText, secretKey);
    System.out.println("密文: " + cipherText);
    String plainText2 = sm4.decryptData_ECB(cipherText, secretKey);
    System.out.println("明文: " + plainText2);
    System.out.println();

    System.out.println("CBC模式加密");
    String cipherText2 = sm4.encryptData_CBC(plainText, secretKey, iv);
    System.out.println("加密密文: " + cipherText2);
    System.out.println();

    String plainText3 = sm4.decryptData_CBC(cipherText2, secretKey, iv);
    System.out.println("解密明文: " + plainText3);

  }

  // ====================================================================================================

  public static byte[] hexStringToBytes(String hexString) {
    if (hexString == null || hexString.equals("")) {
      return null;
    }

    hexString = hexString.toUpperCase();
    int length = hexString.length() / 2;
    char[] hexChars = hexString.toCharArray();
    byte[] d = new byte[length];
    for (int i = 0; i < length; i++) {
      int pos = i * 2;
      d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
    }
    return d;
  }

  public static byte charToByte(char c) {
    return (byte) "0123456789ABCDEF".indexOf(c);
  }

  public static byte[] hexToByte(String hex)
      throws IllegalArgumentException {
    if (hex.length() % 2 != 0) {
      throw new IllegalArgumentException();
    }
    char[] arr = hex.toCharArray();
    byte[] b = new byte[hex.length() / 2];
    for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
      String swap = "" + arr[i++] + arr[i];
      int byteint = Integer.parseInt(swap, 16) & 0xFF;
      b[j] = new Integer(byteint).byteValue();
    }
    return b;
  }

  /**
   * 字节数组转换为十六进制字符串
   *
   * @param b byte[] 需要转换的字节数组
   * @return String 十六进制字符串
   */
  public static String byteToHex(byte[] b) {
    if (b == null) {
      throw new IllegalArgumentException(
          "Argument b ( byte array ) is null! ");
    }
    StringBuilder hs = new StringBuilder();
    String stmp = "";
    for (byte value : b) {
      stmp = Integer.toHexString(value & 0xff);
      if (stmp.length() == 1) {
        hs.append("0").append(stmp);
      } else {
        hs.append(stmp);
      }
    }
    return hs.toString().toLowerCase();
  }
}
