package com.ooooo.unsafe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jdk.internal.misc.Unsafe;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
class UnsafeUserTest {

  private Unsafe unsafe;

  private UnsafeUser user;

  @BeforeEach
  void beforeEach() {
    unsafe = Unsafe.getUnsafe();
    user = new UnsafeUser(1L, "tom", 20);
  }

  @Test
  void testUser() {
    // name
    long nameFieldOffset = unsafe.objectFieldOffset(UnsafeUser.class, "name");
    String name = (String) unsafe.getReference(user, nameFieldOffset);
    assertEquals("tom", name);

    // age
    long ageFieldOffset = unsafe.objectFieldOffset(UnsafeUser.class, "age");
    Integer age = (Integer) unsafe.getReference(user, ageFieldOffset);
    assertEquals(20, age);

    // id
    long idFieldOffset = unsafe.objectFieldOffset(UnsafeUser.class, "id");
    Long id = (Long) unsafe.getReference(user, idFieldOffset);
    assertEquals(1L, id);
  }

  @Test
  void testArray() {
    UnsafeUser[] users = new UnsafeUser[3];
    users[0] = new UnsafeUser(1L, "tom", 20);
    users[1] = new UnsafeUser(2L, "jerry", 21);
    users[1] = new UnsafeUser(3L, "jack", 22);

    int arrBase = unsafe.arrayBaseOffset(UnsafeUser[].class);
    int arrIndexScale = unsafe.arrayIndexScale(UnsafeUser[].class);

    for (int i = 0; i < users.length; i++) {
      UnsafeUser user = (UnsafeUser) unsafe.getReference(users, (long) i * arrIndexScale + arrBase);
      assertEquals(users[i], user);
    }
  }

  @Test
  void testCAS() {
    long nameOffset = unsafe.objectFieldOffset(UnsafeUser.class, "name");
    String oldName = (String) unsafe.compareAndExchangeReference(user, nameOffset, "tom", "jerry");
    assertEquals("tom", oldName);
    assertEquals("jerry", user.getName());

    boolean success = unsafe.compareAndSetReference(user, nameOffset, "jerry", "haha");
    assertTrue(success);
  }

}