package org.smart4j.framework.helper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;

public final class ControllerHelper {
  private static final Map<Request, Handler> ACTION_MAP = new HashMap<>();

  static {
    Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();

    if (CollectionUtils.isNotEmpty(controllerClassSet)) {

      controllerClassSet.forEach(controllerClass -> {
        Method[] methods = controllerClass.getDeclaredMethods();

        Arrays.stream(methods).filter(m -> m.isAnnotationPresent(Action.class)).forEach(actionMethod -> {
          Action action = actionMethod.getAnnotation(Action.class);
          String methodAndPath = action.value();

          if (methodAndPath.matches("\\w:/\\w")) {
            String[] array = StringUtils.split(methodAndPath, ":");

            String requestMethod = array[0];
            String requestPath = array[1];

            Request request = new Request(requestMethod, requestPath);
            Handler handler = new Handler(controllerClass, actionMethod);

            ACTION_MAP.put(request, handler);
          }

        });

      });
    }
  }

  public static Handler getHandler(String requestMethod, String requestPath){
    Request request = new Request(requestMethod, requestPath);
    return ACTION_MAP.get(request);
  }
}
