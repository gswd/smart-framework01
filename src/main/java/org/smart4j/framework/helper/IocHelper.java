package org.smart4j.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ReflectionUtil;

public final class IocHelper {

  static {
    Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();

    beanMap.forEach((key, val) -> {
      Field[] fields = key.getDeclaredFields();
      if (ArrayUtils.isNotEmpty(fields)) {
        for (Field field : fields) {
          if (!field.isAnnotationPresent(Inject.class)) {
            continue;
          }
          Class<?> beanFieldClass = field.getType();
          Object beanInstance = BeanHelper.getBean(beanFieldClass);
          if (Objects.nonNull(beanInstance)) {
            ReflectionUtil.setField(val, field, beanInstance);
          }
        }
      }
    });
  }
}
