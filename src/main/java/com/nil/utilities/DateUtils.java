package com.nil.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public abstract class DateUtils {

  private final static Pattern datePattern =
      Pattern.compile("(\\d){4}\\-(\\d){2}\\-(\\d){2}T(\\d){2}:(\\d){2}:(\\d){2}\\.(\\d){3}Z");
  private final static Pattern sqlDatePattern =
      Pattern.compile("(\\d){4}\\-(\\d){2}\\-(\\d){2}T(\\d){2}:(\\d){2}:(\\d){2}\\.(\\d){3}[\\+|\\-](\\d){4}");

  public final static String getTimeStampString(final String format) {
    return getTimeStampString(java.util.Locale.KOREA, format);
  }

  public final static String getTimeStampString(final Locale locale, final String format) {
    return getTimeStampString(new Date(), locale, format);
  }

  public final static String getTimeStampString(final Date date, final String format) {
    return getTimeStampString(date, java.util.Locale.KOREA, format);
  }

  public final static String getTimeStampString(final long timestamp, final String format) {
    return getTimeStampString(new Date(timestamp), format);
  }

  public final static String getTimeStampString(final Date date, final Locale locale, final String format) {
    return new java.text.SimpleDateFormat(format, locale).format(date);
  }

  public final static String getTimeStampString(final ZonedDateTime date, final String format) {
    return getTimeStampString(date, java.util.Locale.KOREA, format);
  }

  public final static String getTimeStampString(final ZonedDateTime date, final Locale locale, final String format) {
    return getTimeStampString(java.sql.Date.from(date.toInstant()), locale, format);
  }

  public final static Date getOffsetDate(final long offset) {
    OffsetDateTime utc = OffsetDateTime.now(ZoneOffset.ofTotalSeconds((int)offset * 60));
    return Date.from(utc.toInstant());
  }

  public final static Date stringToDate(String date) throws ParseException {
    if(StringUtils.hasEmpty(date)) return null;

    date	= date.replaceAll("[^\\d]", "");

    if(14 < date.length()) date = date.substring(0, 14);
    switch(date.length()) {
      case 14: break;
      case 12: date += "00"; break;
      case 10: date += "0000"; break;
      case 8:	 date += "000000"; break;
      case 6:	 date += "01000000"; break;
      case 4:	 date += "0101000000"; break;
      default: return null;
    }

    final var tmpFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss", java.util.Locale.KOREA);
    tmpFormat.setLenient(true);

    return tmpFormat.parse(date);
  }

  public final static ZonedDateTime stringToZonedDatetime(String date) throws ParseException {
    ZonedDateTime zonedDateTime;

    if (datePattern.matcher(date).find()) {
      zonedDateTime = ZonedDateTime.parse(date);
    } else if(sqlDatePattern.matcher(date).find()) {
      zonedDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
          .parse(date)
          .toInstant().atZone(ZoneId.systemDefault());
    } else {
      zonedDateTime =
          ZonedDateTime.ofInstant(stringToDate(date).toInstant(), ZoneId.of("UTC"));
    }

    return zonedDateTime;
  }

  public final static String stringToZonedDatetime(String date, String format) throws ParseException {
    return getTimeStampString(stringToZonedDatetime(date), format);
  }

  public final static String getAfterDay(final int date) {
    return getAfterDay(date, "yyyy-MM-dd");
  }
  public final static String getAfterDay(final int date, final String format) {
    Calendar cal	= Calendar.getInstance();
    cal.add(Calendar.DATE, date);

    return getTimeStampString(cal.getTime(), format);
  }
  public final static String getAfterMonth(final int month) {
    return getAfterMonth(month, "yyyy-MM-dd");
  }
  public final static String getAfterMonth(final int month, final String format) {
    Calendar	cal	= Calendar.getInstance();
    cal.add(Calendar.MONTH, month);

    return getTimeStampString(cal.getTime(), format);
  }
  public final static java.util.Date getAfterMonth(final java.util.Date current, final int after) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(current);
    cal.add(Calendar.MONTH, after);

    return cal.getTime();
  }

  public final static String getFirstDay(final String when) {
    return getFirstDay(when, "yyyy-MM-dd");
  }
  public final static String getFirstDay(final String when, final String format) {
    Date current;
    try {
      current = stringToDate(when);
    } catch(ParseException e) {
      current = new Date();
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(current);
    cal.set(Calendar.DAY_OF_MONTH, 1);

    return getTimeStampString(cal.getTime(), format);
  }

  public final static String getLastDay(final String when, final String format) {
    if(6 == when.length())
      return getLastDay(when.substring(0, 4), when.substring(4), format);

    return getLastDay(getTimeStampString("yyyy"), when, format);
  }
  public final static String getLastDay(final String year, final String month, final String format) {
    return getLastDay(Integer.parseInt(year), Integer.parseInt(month), format);
  }
  public final static String getLastDay(final int year, final int month, final String format) {
    Calendar	cal	= Calendar.getInstance();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

    return getTimeStampString(cal.getTime(), format);
  }

  public final static boolean hasValidDate(final String ymd) {
    if (!ymd.matches("^(\\d{4}).?(\\d{2}).?(\\d{2})$")) return false;

    try {
      LocalDate.parse(ymd.replaceAll("[^\\d]", ""), DateTimeFormatter.BASIC_ISO_DATE);
      return true;
    } catch (DateTimeParseException e) {
      // do nothing
    }

    return false;
  }

}
