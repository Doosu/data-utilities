package com.nil.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nil.utilities.StringUtils;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

  @Test
  public void toCamelCase() {
    assertEquals("schoolBusDriver", StringUtils.toCamelCase("school_bus_driver"));
    assertEquals("goTheHell", StringUtils.toCamelCase("GO_THE_HELL"));
  }

}
