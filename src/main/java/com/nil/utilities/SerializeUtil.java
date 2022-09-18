package com.nil.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {

  public static final String serialized(final Object data) throws IOException {
    try (
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(os)
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
        ByteArrayInputStream is = new ByteArrayInputStream(StringUtils.decodeBase64(serialized));
        ObjectInputStream ois = new ObjectInputStream(is)
    ) {
      return type.cast(ois.readObject());
    } catch (IOException | ClassNotFoundException e) {
      throw e;
    }
  }

}
