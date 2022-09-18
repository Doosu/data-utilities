package com.nil.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nil.utilities.CommonUtils;
import com.nil.utilities.KeyGeneratorUtils;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class CommonUtilsTest {

  @Test
  public void isEmpty() {
    final String value = 0 < System.currentTimeMillis() ? null : "EMPTY";

    assertTrue(CommonUtils.isEmpty(value));
  }

  @Test
  public void isAnyEmpty() {
    final Object[] values = Stream.of("a", "b", "", "c", "d").toArray(Object[]::new);

    assertTrue(CommonUtils.isAnyEmpty(values));
  }

  @Test
  public void isPrimitive() {
    assertTrue(CommonUtils.isPrimitive(""));
    assertTrue(CommonUtils.isPrimitive(1L));
    assertTrue(CommonUtils.isPrimitive(false));
    assertTrue(CommonUtils.isPrimitive(3.0F));
    assertFalse(CommonUtils.isPrimitive(Stream.of()));
    assertFalse(CommonUtils.isPrimitive(Arrays.asList()));
  }

  @Test
  public void isIterable() {
    assertTrue(CommonUtils.isIterable(Stream.of()));
    assertTrue(CommonUtils.isIterable(new String[3]));
    assertFalse(CommonUtils.isIterable(""));
    assertFalse(CommonUtils.isIterable(3D));
  }

  @Test
  public void nvl() {
    assertEquals("3", CommonUtils.nvl(null, "3", String.class));
    assertEquals("ASDF", CommonUtils.nvl("ASDF", null, String.class));
    assertNotEquals(3L, CommonUtils.nvl("3", null, String.class));
  }

  @Test
  public void decode() {
    assertEquals("2", CommonUtils.decode(5, 3, "1", 5, "2", 7, "4"));
    assertEquals("def", CommonUtils.decode(0, 1, "red", 2, "yellow", "def"));
    assertEquals("apple", CommonUtils.decode("Notebook", "pen", "banana", "notebook", "peach", "Notebook", "apple"));
  }

  @Test
  public void rsaEncrypt() {
    final KeyPair keyPair = KeyGeneratorUtils.generateRsaKey();
    final String text = "Encryption test string";
    final String encrypted = CommonUtils.rsaEncrypt(text, keyPair.getPublic());
    final String decrypted = CommonUtils.rsaDecrypt(encrypted, keyPair.getPrivate());

    System.out.println(encrypted);
    System.out.println(decrypted);

    assertNotNull(encrypted);
    assertEquals(text, decrypted);
  }



}
