package org.smart4j.framework.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.framework.util.ReflectionUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AopHelper {

  static {
    Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
    Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);

    targetMap.forEach((targetClass, proxyList) -> {
      Object proxy = ProxyManager.createProxy(targetClass, proxyList);
      BeanHelper.setBean(targetClass, proxy);
    });
  }

  /**
   * 切面classType -> [被增强classType]
   *
   */
  private static Map<Class<?>,Set<Class<?>>> createProxyMap() {
    Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<>();
    Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);
    proxyClassSet.stream().filter(p -> p.isAnnotationPresent(Aspect.class)).forEach(p -> {
      Aspect aspect = p.getAnnotation(Aspect.class);
      Class<? extends Annotation> annotation = aspect.value();
      if (!Aspect.class.equals(annotation)) {
        proxyMap.put(p, ClassHelper.getClassSetByAnnotation(annotation));
      }

    });
    return proxyMap;
  }

  private static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap){
    Map<Class<?>, List<Proxy>> targetMap = new HashMap<>();

    for(Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()){

      Class<?> proxyClass = proxyEntry.getKey();
      Set<Class<?>> targetClassSet = proxyEntry.getValue();

      Proxy proxy = (Proxy)ReflectionUtil.newInstance(proxyClass);

      targetClassSet.forEach(targetClass -> {
        if(targetMap.containsKey(targetClass)){
          targetMap.get(targetClass).add(proxy);
        }else {
          List<Proxy> proxyList = new ArrayList<>();
          proxyList.add(proxy);
          targetMap.put(targetClass, proxyList);
        }
      });

    }
    return targetMap;
  }
}
