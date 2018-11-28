package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

import lombok.NonNull;

public final class ClassHelper {
  private static final Set<Class<?>> CLASS_SET;

  static {
    String basePackage = ConfigHelper.getAppBasePackage();
    CLASS_SET = ClassUtil.getClassSet(basePackage);
  }

  public static Set<Class<?>> getClassSet() {
    return CLASS_SET;
  }

  public static Set<Class<?>> getServiceClassSet() {
    return getClassSetByAnnotation(Service.class);
  }

  public static Set<Class<?>> getControllerClassSet() {
    return getClassSetByAnnotation(Controller.class);
  }

  public static Set<Class<?>> getBeanClassSet() {
    return Stream.concat(getServiceClassSet().stream(), getControllerClassSet().stream()).collect(Collectors.toSet());
  }

  public static Set<Class<?>> getClassSetBySuper(@NonNull Class<?> superClass) {
    return CLASS_SET.stream().filter(c -> c.isAssignableFrom(c) && !superClass.equals(c)).collect(Collectors.toSet());
  }

  public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass) {
    return CLASS_SET.stream().filter(c -> c.isAnnotationPresent(annotationClass)).collect(Collectors.toSet());
  }
}
