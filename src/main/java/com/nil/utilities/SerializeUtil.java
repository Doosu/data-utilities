package com.nil.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

  public static final String serialized(final Object data) throws IOException {
    try (
        var os = new ByteArrayOutputStream();
        var oos = new ObjectOutputStream(os)
    ) {
      oos.writeObject(data);

      return StringUtils.encodeBase64(os.toByteArray());
    } catch(IOException e) {
      throw e;
    }
  }

  public static final <T> T deserialized(String serialized, Class<T> type)
      throws IOException, ClassNotFoundException {
    try (
        var is = new ByteArrayInputStream(StringUtils.decodeBase64(serialized));
        var ois = new ObjectInputStream(is)
    ) {
      return type.cast(ois.readObject());
    } catch (IOException | ClassNotFoundException e) {
      throw e;
    }
  }

}
