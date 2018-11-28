package org.smart4j.framework.proxy;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

import lombok.Getter;
import lombok.NonNull;

public class ProxyChain {

  @Getter
  private final Class<?> targetClass;
  private final Object targetObject;
  @Getter
  private final Method targetMethod;
  private final MethodProxy methodProxy;
  @Getter
  private final Object[] methodParams;

  private List<Proxy> proxyList;
  private int proxyIndex = 0;

  public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, @NonNull List<Proxy> proxyList) {
    this.targetClass = targetClass;
    this.targetObject = targetObject;
    this.targetMethod = targetMethod;
    this.methodProxy = methodProxy;
    this.methodParams = methodParams;
    this.proxyList = proxyList;
  }

  public Object doProxyChain() throws Throwable {
    if (proxyIndex < proxyList.size()) {
      return proxyList.get(proxyIndex).doProxy(this);
    }

    return methodProxy.invokeSuper(targetObject, methodParams);
  }

}
