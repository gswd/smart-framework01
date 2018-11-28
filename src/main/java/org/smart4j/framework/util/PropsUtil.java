package org.smart4j.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PropsUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);


  public static Properties loadProps(String fileName) {

    try(InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)){
      Properties properties = new Properties();
      properties.load(is);
      return properties;

    } catch (IOException e) {
      LOGGER.error("load properties file failure", e);
      return null;
    }
  }

  public static String getString(Properties props, String key) {
    return PropsUtil.getString(props, key, StringUtils.EMPTY);
  }
  public static String getString(Properties props, String key, String defaultValue) {
    Objects.requireNonNull(props);
    return props.getProperty(key, defaultValue);
  }

  public static int getInt(Properties props, String key) {
    return PropsUtil.getInt(props, key, 0);
  }

  public static int getInt(Properties props, String key, int defaultValue) {
    Objects.requireNonNull(props);
    String val = props.getProperty(key);
    return (val == null) ? defaultValue : CastUtil.castInt(val);
  }

  public static boolean getBoolean(Properties props, String key) {
    return PropsUtil.getBoolean(props, key, false);
  }

  public static boolean getBoolean(Properties props, String key, boolean defaultValue) {
    Objects.requireNonNull(props);
    String val = props.getProperty(key);
    return (val == null) ? defaultValue : CastUtil.castBoolean(val);
  }


}
