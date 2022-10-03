package com.ooooo.zip;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.util.StreamUtils;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/1/21 13:37
 * @since 1.0.0
 */
public class ZipUtil {

  @SneakyThrows
  public static void toZipEntry(ZipOutputStream zipOutputStream, String entryPath, byte[] data) {
    ZipEntry entry = new ZipEntry(entryPath);
    zipOutputStream.putNextEntry(entry);
    zipOutputStream.write(data);
  }

  @SneakyThrows
  public static Pair<String, byte[]> formZipEntry(ZipInputStream zipInputStream) {
    ZipEntry entry = zipInputStream.getNextEntry();
    if (entry == null) {
      return null;
    }
    String entryName = entry.getName();
    byte[] bytes = StreamUtils.copyToByteArray(zipInputStream);
    return Pair.of(entryName, bytes);
  }

  @Data
  @AllArgsConstructor
  public static class Pair<K, V> {

    private K key;
    private V value;

    public static <K, V> Pair<K, V> of(K k, V v) {
      return new Pair<>(k, v);
    }
  }

}
