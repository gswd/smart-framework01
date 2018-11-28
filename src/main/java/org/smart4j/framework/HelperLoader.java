package org.smart4j.framework;

import org.smart4j.framework.helper.AopHelper;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.helper.IocHelper;
import org.smart4j.framework.util.ClassUtil;

public final class HelperLoader {

  public static void init() {
    // AopHelper需要在IocHelper之前加载，首先通过AopHelper获取代理对象，然后通过AopHelper依赖注入
    Class<?>[] classList = {
      ClassHelper.class, BeanHelper.class, AopHelper.class, IocHelper.class, ControllerHelper.class
    };

    for (Class<?> cls : classList) {
      ClassUtil.loadClass(cls.getName(), true);
    }
  }
}
