package com.nil.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringUtils {

  public static final String EMPTY_STRING = "";
  public static final String TOKEN_SPLIT_CHAR = " ";

  /**
   * 주어진 문자열이 빈 문자인지 확인 합니다
   * @param value
   * @return
   */
  public static final boolean hasEmpty(final CharSequence value) {
    return null == value || EMPTY_STRING.equals(value.toString().trim());
  }

  /**
   * 주어진 문자열이 비었다면 기본값으로 전달된 값을 반환 합니다
   * @param obj
   * @param def
   * @return
   */
  public static final String nvl(final Object obj, final String def) {
    return CommonUtils.nvl(obj, def, String.class);
  }

  /**
   * 주어진 객체를 문자열로 반환합니다. 객체가 정의되지 않은 경우 빈 문자열을 반환합니다.
   * @param obj
   * @return
   */
  public static final String nvl(final Object obj) {
    return nvl(obj, EMPTY_STRING);
  }

  /**
   * 주어진 문자열에서 oldPart 찾아 newPart 변환한 후 반환합니다
   * @param source
   * @param oldPart
   * @param newPart
   * @return
   */
  public static final String replace(final String source, final String oldPart, final String newPart) {
    if(source == null) return "";
    if(oldPart == null || newPart == null) return source;

    StringBuilder sb = new StringBuilder();
    int last = 0;
    while (true) {
      int start = source.indexOf(oldPart, last);
      if (start >= 0) {
        sb.append(source.substring(last, start));
        sb.append(newPart);
        last = start + oldPart.length();
      }
      else {
        sb.append(source.substring(last));
        return sb.toString();
      }
    }
  }

  /**
   * 주어진 문자열에서 정규식에 매치되는 문자열을 전달된 replacer 함수를 호출하여 변환후 반환합니다
   * @param source
   * @param pattern
   * @param replacer
   * @return
   */
  public final static String replace(
      final String source, final Pattern pattern, final Function<Matcher, String> replacer
  ) {
    final Matcher matcher = pattern.matcher(source);
    final StringBuffer result = new StringBuffer();

    while(matcher.find()) {
      matcher.appendReplacement(result, replacer.apply(matcher));
    }
    matcher.appendTail(result);

    return result.toString();
  }

  /**
   * TAG 표시용 문자로 변환 합니다
   * @param str
   * @return
   */
  public final static String safeHTML(String str) {
    str	= replace(str, "&", "&amp;");
    str	= replace(str, "#", "&#35;");
    str	= replace(str, "<", "&lt;");
    str	= replace(str, ">", "&gt;");
    str	= replace(str, "(", "&#40;");
    str	= replace(str, ")", "&#41;");
    str	= replace(str, "\"", "&quot;");
    str	= replace(str, "\'", "&#39;");

    return str;
  }

  /**
   * TAG 표시용 문자를 복원 합니다
   * @param str
   * @return
   */
  public static final String deSafeHTML(String str) {
    if (CommonUtils.isEmpty(str)) return "";

    str	= replace(str, "&#39;", "'");
    str	= replace(str, "&quot;", "\"");
    str	= replace(str, "&#41;", ")");
    str	= replace(str, "&#40;", "(");
    str	= replace(str, "&gt;", ">");
    str	= replace(str, "&lt;", "<");
    str	= replace(str, "&#35;", "#");
    str	= replace(str, "&amp;", "&");

    return str;
  }

  /**
   * 중복된 줄바꿈을 하나로 변환합니다
   * @param text
   * @return
   */
  public static final String normalizeWhitespace(final String text) {
    return nvl(text).replaceAll("(\\r\\n|\\r|\\n)(\\s*(\\r\\n|\\r|\\n))+", "$1");
  }

  /**
   * 주어진 문자열을 URL safe string 변환합니다
   * @param text
   * @return
   */
  public static final String encodeUrl(final String text) {
    try {
      return URLEncoder.encode(text, CommonUtils.DEFAULT_ENCODING).replaceAll("\\+", "%20");
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }

  /**
   * 주어진 URL safe 문자열을 복원합니다
   * @param text
   * @return
   */
  public static final String decodeUrl(final String text) {
    try {
      return URLDecoder.decode(text, CommonUtils.DEFAULT_ENCODING);
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }

  /**
   * 주어진 문자열을 Base64 문자열로 인코딩하여 반환 합니다
   * @param text
   * @return
   */
  public static final String encodeBase64(final String text) {
    return encodeBase64(nvl(text).getBytes(CommonUtils.DEFAULT_CHARSET));
  }
  /**
   * 주어진 바이트 배열을 Base64 문자열로 인코딩하여 반환 합니다
   * @param source
   * @return
   */
  public static final String encodeBase64(final byte[] source) {
    return Base64.getEncoder().encodeToString(source);
  }

  /**
   * 주어진 문열이 Base64 형식 인코딩된 문자열이라면 복원하여 바이트 배열로 반환 합니다.
   * @param text
   * @return
   */
  public static final byte[] decodeBase64(final String text) {
    try {
      return Formatters.isBase64String(text)
          ? Base64.getDecoder().decode(text.getBytes(CommonUtils.DEFAULT_ENCODING))
          : text.getBytes(CommonUtils.DEFAULT_ENCODING); // throw new IllegalArgumentException();
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 주어진 문열이 Base64 형식 인코딩된 문자열이라면 복원하여 문자열로 반환 합니다
   * @param text
   * @return
   */
  public static final String decodeBase64String(final String text) {
    if (CommonUtils.isEmpty(text)) return EMPTY_STRING;
    if (!Formatters.isBase64String(text)) return text;

    try {
      return new String(decodeBase64(text), CommonUtils.DEFAULT_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * 주어진 문자열을 camel case 형태로 변환하여 반환 합니다
   * @param source
   * @return
   */
  public final static String toCamelCase(final String source) {
    return Formatters.CAMEL_PATTERN.matcher(source.toLowerCase()).find()
        ? replace(source.toLowerCase(), Formatters.CAMEL_PATTERN, (m) -> m.group(1).toUpperCase())
        : source;
  }

  /**
   * 주어진 문자열을 주어진 숫자만큼 반복하여 연결하여 반환합니다
   * @param in
   * @param count
   * @return
   */
  public final static String repeat(final String in, final int count) {
    final StringBuilder	out	= new StringBuilder(in.length() * count);

    for(int i = 0;i < count;i++) out.append(in);

    return out.toString();
  }

  /**
   * 주어진 문자열을 주어진 횟수만큼 섞습니다
   * @param in
   * @return
   */
  public final static String shuffle(final String in) {
    return shuffle(in, 1);
  }
  public final static String shuffle(final String in, final int loop) {
    final StringBuilder out = new StringBuilder(in);
    final ArrayList<Character> chars = new ArrayList<>();

    for(int i = 0;i < loop;i++) {
      final String org = out.toString();

      out.setLength(0);
      for(final char c : org.toCharArray())
        chars.add(c);

      while(!chars.isEmpty())
        out.append(chars.remove((int)(Math.random() * chars.size())));
    }

    return out.toString();
  }

  /**
   * 랜덤한 숫자를 지정된 길이에 맞는 문자열로 반환합니다
   * @param length
   * @return
   */
  public final static String getRandomNumberString(final int length) {
    return shuffle(repeat(shuffle("1234567890"), 5), 5).substring(0, length);
  }

  /**
   * 랜덤한 영문/숫자를 지정된 길이에 맞는 문자열로 반환합니다 (특수문자 포함 옵션)
   * @param length
   * @param isSpecialChars
   * @return
   */
  public final static String getRandomString(final int length, final boolean isSpecialChars) {
    String str	= "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890" +
        (isSpecialChars ? "`~!@#$%^&*()_+<>,./" : "");
    str	= shuffle(str);
    str	= repeat(str, 5);
    str	= shuffle(str);

    return str.substring(0, length);
  }

}
