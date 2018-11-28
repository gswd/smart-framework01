package org.smart4j.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smart4j.framework.util.ReflectionUtil;

import com.sun.istack.internal.NotNull;
import lombok.NonNull;

public final class BeanHelper {
  private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

  static {

    Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
    beanClassSet.forEach(beanClass -> BEAN_MAP.put(beanClass, ReflectionUtil.newInstance(beanClass)));
  }

  public static Map<Class<?>, Object> getBeanMap() {
    return BEAN_MAP;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getBean(Class<T> cls) {
    if (!BEAN_MAP.containsKey(cls)) {
      throw new RuntimeException("can not get bean by class: " + cls);
    }
    return (T)BEAN_MAP.get(cls);
  }

  public static void setBean(@NonNull Class<?> cls, @NotNull Object obj) {
    BEAN_MAP.put(cls, obj);
  }
}
