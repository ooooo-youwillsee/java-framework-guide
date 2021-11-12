package com.ooooo.util;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;


/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/12 11:27
 * @since 1.0.0
 */
public class EncryptUtil {
	
	public static String aesEncryptByCBC(String content, String encryptKey) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		
		SecretKeySpec secretKeySpec = new SecretKeySpec(encryptKey.getBytes(StandardCharsets.UTF_8), "AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(encryptKey.getBytes(StandardCharsets.UTF_8)));
		byte[] bytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
		return Base64.encodeBase64String(bytes);
	}
	
	public static String aesDecryptByCBC(String encryptStr, String decryptKey) throws Exception {
		byte[] encryptBytes = Base64.decodeBase64(encryptStr);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		SecretKeySpec secretKeySpec = new SecretKeySpec(decryptKey.getBytes(StandardCharsets.UTF_8), "AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(decryptKey.getBytes(StandardCharsets.UTF_8)));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes);
		
	}
}
