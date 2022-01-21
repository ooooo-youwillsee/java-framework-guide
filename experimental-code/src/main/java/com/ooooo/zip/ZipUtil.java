package com.ooooo.zip;

import java.util.jar.JarEntry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/1/21 13:37
 * @since 1.0.0
 */
public class ZipUtil {

  @SneakyThrows
  public static void toZipEntry(ZipOutputStream zipOutputStream, String entryPath, byte[] data) {
    JarEntry entry = new JarEntry(entryPath);
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
    byte[] bytes = IOUtils.toByteArray(zipInputStream);
    return Pair.of(entryName, bytes);
  }

}
