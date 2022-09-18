package com.nil.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nil.utilities.DateUtils;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

public class DateUtilsTest {

  @Test
  public void getTimeStampString() {
    assertEquals(LocalDate.now().toString(), DateUtils.getTimeStampString("yyyy-MM-dd"));
  }

}
