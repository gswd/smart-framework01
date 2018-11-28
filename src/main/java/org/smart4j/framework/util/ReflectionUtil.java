package org.smart4j.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ReflectionUtil {
  private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

  public static Object newInstance(Class<?> cls) {
    Object instance;

    try {
      instance = cls.newInstance();
    } catch (Exception e) {
      logger.error("new instance failure", e);
      throw new RuntimeException(e);
    }
    return instance;
  }

  public static Object invokeMethod(Object obj, Method method, Object... args) {

    method.setAccessible(true);
    try {
      Object result = method.invoke(obj, args);
      return result;
    } catch (Exception e) {
      logger.error("invoke method failure", e);
      throw new RuntimeException(e);
    }
  }

  public static void setField(Object obj, Field field, Object value) {
    try {
      field.setAccessible(true);
      field.set(obj, value);
    } catch (IllegalAccessException e) {
      logger.error("set field failure", e);
      throw new RuntimeException(e);
    }

  }
}
