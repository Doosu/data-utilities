package com.nil.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nil.utilities.AsyncUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.junit.jupiter.api.Test;

public class AsyncUtilsTest {

  @Test
  public void promiseTest() {
    final List<Object> bodies = AsyncUtils.promiseAll(
//        "https://api.publicapis.org/entries",
        () -> request("http://petstore-demo-endpoint.execute-api.com/petstore/pets"),
        () -> request("https://api.coindesk.com/v1/bpi/currentprice.json"),
        () -> request("https://datausa.io/api/data?drilldowns=Nation&measures=Population")
    );

    assertEquals(3, bodies.size());
    assertNotNull(bodies.get(0));
    assertNotNull(bodies.get(1));
    assertNotNull(bodies.get(2));

    bodies.stream().forEach(System.out::println);
  }

  private String request(final String value) {
    try {
      final URL url = new URL(value);
      final URLConnection connection = url.openConnection();
      final StringBuilder body = new StringBuilder();
      try (
          final InputStreamReader is = new InputStreamReader(connection.getInputStream());
          final BufferedReader br = new BufferedReader(is)
      ) {
        br.lines().forEach(body::append);
      }

      return body.toString();
    } catch (IOException e) {
      return null;
    }
  }

}
