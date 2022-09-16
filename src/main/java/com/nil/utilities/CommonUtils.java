package com.nil.utilities;

import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.crypto.Cipher;

public abstract class CommonUtils {

  public static final Locale DEFAULT_LOCALE = Locale.KOREA;
  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
  public static final String DEFAULT_ENCODING = DEFAULT_CHARSET.toString();

  /**
   * 주어진 객체가 비었는지 확인합니다
   * @param obj
   * @return
   */
  public static final boolean isEmpty(final Object obj) {
    return null == obj
        || (obj instanceof String && StringUtils.hasEmpty(((String) obj).trim()))
        || (obj instanceof List && ((List<?>)obj).isEmpty())
        || (obj instanceof Map && ((Map<?,?>)obj).isEmpty())
        || (obj instanceof Object[] && 0 >= Array.getLength(obj))
        || (obj instanceof Integer && 0 == (Integer) obj)
        || (obj instanceof Long && 0 == (Long) obj);
  }

  /**
   * 전달된 모든 객체중 하나라도 비어있는 객체가 있는지 확인합니다
   * @param objects
   * @return
   */
  public static final boolean isAnyEmpty(final Object ... objects) {
    return Stream.of(objects).anyMatch(CommonUtils::isEmpty);
  }

  /**
   * 주어진 값이 true 인지 확인 합니다
   * @param obj
   * @return
   */
  public static final boolean isTrue(final Object obj) {
    return !isEmpty(obj)
        && (
              (obj instanceof Boolean && (Boolean) obj) ||
              (obj instanceof Number && 0 < ((Number) obj).intValue()) ||
              (obj instanceof String &&
                  !StringUtils.EMPTY_STRING.equals(obj) && Boolean.parseBoolean((String) obj))
           );
  }

  /**
   * 주어진 객체가 원시형 데이터 객체 유형인지 확인 합니다
   * @param obj
   * @return
   */
  public static final boolean isPrimitive(final Object obj) {
    return obj instanceof CharSequence || obj instanceof Number || obj instanceof Boolean;
  }

  /**
   * 주어진 객체가 반복가능한 객체인지 확인 합니다
   * @param o
   * @return
   */
  public static final boolean isIterable(final Object o) {
    return null != o && (
        o.getClass().isArray() ||
            Iterable.class.isAssignableFrom(o.getClass()) ||
            Iterator.class.isAssignableFrom(o.getClass()) ||
            Enumeration.class.isAssignableFrom(o.getClass())
    );
  }

  /**
   * 주어진 객체를 지정된 유형으로 형변환 합니다. 주어진 객체가 비어있다면 기본 값의 객체를 반환합니다
   * @param obj
   * @param def
   * @param type
   * @return
   * @param <T>
   */
  public static final <T> T nvl(final Object obj, final Object def, final Class<T> type) {
    return type.cast(isEmpty(obj) ? def : obj);
  }

  /**
   * 주어진 값에 참이면 다음 객체를 반환합니다. (like oracle decode)
   * @param args
   * @return
   */
  public static Object decode(final Object ... args) {
    if (1 >= args.length) return "";

    final var val = args[0];
    final var def = 0 == args.length % 2 ? args[args.length - 1] : "";
    final var wheres = IntStream.range(1, args.length - 1).filter(i -> 1 == i % 2)
        .mapToObj(i -> args[i]).collect(Collectors.toList());
    final var values = IntStream.range(1, args.length - 1).filter(i -> 0 == i % 2)
        .mapToObj(i -> args[i]).collect(Collectors.toList());

    return IntStream.range(0, wheres.size()).filter(i -> val.equals(wheres.get(i)))
        .mapToObj(values::get).findFirst().orElse(def);
  }

  /**
   * 나열된 배열 객체의 홀수 인자는 키, 짝수 인자는 값으로 갖는 Map 객체를 반환합니다
   * @param args
   * @return
   */
  public static final Map<Object, Object> ofEntries(final Object ... args) {
    if (1 >= args.length) return Collections.EMPTY_MAP;

    return IntStream.range(0, (int) Math.ceil(args.length / 2D))
        .boxed()
        .collect(Collectors.toMap(i -> args[2 * i], i -> args[2 * i + 1]));
  }

  /**
   * RSA 비대칭 키로 대상 문자열을 암호화 후 Base64 encoding 하여 반환합니다
   * @param plainText
   * @param key
   * @return
   */
  public static String rsaEncrypt(final String plainText, final Key key) {
    try {
      final var cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, key);

      return StringUtils.encodeBase64(cipher.doFinal(plainText.getBytes()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * RSA 비대칭 키로 전달된 암호화 문자열을 Base64 Decode 후 복호화 하여 반환합니다.
   * @param encodedText
   * @param key
   * @return
   */
  public static String rsaDecrypt(final String encodedText, final Key key) {
    try {
      final var cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, key);
      final var urlSafeBase64 = StringUtils.replace(
          StringUtils.replace(URLDecoder.decode(encodedText, DEFAULT_CHARSET), "%20", "+"),
          " ", "+"
      );
      final var decoded = cipher.doFinal(StringUtils.decodeBase64(urlSafeBase64));
      return new String(decoded, DEFAULT_CHARSET);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
