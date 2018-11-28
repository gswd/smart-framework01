package org.smart4j.framework.proxy;

import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AspectProxy implements Proxy{
  @Override
  public Object doProxy(ProxyChain proxyChain) throws Throwable {

    Class<?> cls = proxyChain.getTargetClass();
    Method method = proxyChain.getTargetMethod();
    Object[] params = proxyChain.getMethodParams();

    begin();

    try {

      if (intercept(cls, method, params)) {
        before(cls, method, params);
        Object result = proxyChain.doProxyChain();
        after(cls, method, params);

        return result;
      }

      return proxyChain.doProxyChain();

    } catch (Exception e) {
      log.error("proxy failure", e);
      error(cls, method, params, e);
      throw e;
    } finally {
      end();
    }
  }

  public void end() {
  }

  public void error(Class<?> cls, Method method, Object[] params, Exception e) {
  }

  public void after(Class<?> cls, Method method, Object[] params) {
  }

  public void before(Class<?> cls, Method method, Object[] params) {
  }

  public boolean intercept(Class<?> cls, Method method, Object[] params){
    return true;
  }

  public void begin(){}

}
