package com.ooooo.zip;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ooooo.zip.ZipUtil.Pair;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2022/1/21 14:15
 * @since 1.0.0
 */
class ZipUtilTest {


  @SneakyThrows
  @Test
  public void testZip() {
    // test toZipEntiry
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

    ZipUtil.toZipEntry(zipOutputStream, "xxx/yyy.txt", "111".getBytes(StandardCharsets.UTF_8));
    ZipUtil.toZipEntry(zipOutputStream, "xxx/zzz.txt", "222".getBytes(StandardCharsets.UTF_8));
    zipOutputStream.flush();
    zipOutputStream.close();

    // test fromZipEntry
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);

    Pair<String, byte[]> pair1 = ZipUtil.formZipEntry(zipInputStream);
    assertEquals("xxx/yyy.txt", pair1.getKey());
    assertEquals("111", new String(pair1.getValue(), StandardCharsets.UTF_8));

    Pair<String, byte[]> pair2 = ZipUtil.formZipEntry(zipInputStream);
    assertEquals("xxx/zzz.txt", pair2.getKey());
    assertEquals("222", new String(pair2.getValue(), StandardCharsets.UTF_8));

    zipInputStream.close();
  }

}