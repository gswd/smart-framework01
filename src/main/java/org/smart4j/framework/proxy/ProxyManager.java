package org.smart4j.framework.proxy;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class ProxyManager {

  @SuppressWarnings("unchecked")
  public static <T> T createProxy(Class<?> targetClass, List<Proxy> proxyList) {
    return (T)Enhancer.create(targetClass, new MethodInterceptor() {
      @Override
      public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        return new ProxyChain(targetClass, obj, method, proxy, args, proxyList).doProxyChain();
      }
    });
  }
}
