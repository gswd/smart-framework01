package org.smart4j.framework.util;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public final class CastUtil {

  public static String castString(Object obj) {
    return CastUtil.castString(obj, "");

  }

  public static String castString(Object obj, String defaultValue) {
    return Objects.nonNull(obj) ? String.valueOf(obj) : defaultValue;
  }

  public static double castDouble(Object obj) {
    return CastUtil.castDouble(obj, 0);
  }

  public static double castDouble(Object obj, double defaultValue) {
    if (Objects.isNull(obj)) {
      return defaultValue;
    }

    String doubleStr = CastUtil.castString(obj);
    if (StringUtils.isBlank(doubleStr)) {
      return defaultValue;
    }

    try {

      return Double.parseDouble(doubleStr);

    } catch (NumberFormatException e) {
      return defaultValue;
    }

  }

  public static long castLong(Object obj) {
    return CastUtil.castLong(obj, 0L);
  }

  public static long castLong(Object obj, long defaultValue) {
    if (Objects.isNull(obj)) {
      return defaultValue;
    }

    String longStr = CastUtil.castString(obj);

    if (StringUtils.isBlank(longStr)) {
      return defaultValue;
    }

    try {
      return Long.parseLong(longStr);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }


  public static int castInt(Object obj) {
    return CastUtil.castInt(obj, 0);
  }

  public static int castInt(Object obj, int defaultValue) {
    if (Objects.isNull(obj)) {
      return defaultValue;
    }

    String intStr = CastUtil.castString(obj);

    if (StringUtils.isBlank(intStr)) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(intStr);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  public static boolean castBoolean(Object obj) {
    return CastUtil.castBoolean(obj, false);
  }

  public static boolean castBoolean(Object obj, boolean defaultValue) {

    if (Objects.isNull(obj)) {
      return defaultValue;
    }

    return Boolean.parseBoolean(CastUtil.castString(obj));
  }
}
