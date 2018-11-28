package org.smart4j.framework;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.util.ReflectionUtil;

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {
  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    HelperLoader.init();

    ServletContext servletContext = servletConfig.getServletContext();

    ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
    jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");

    ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
    defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String requestMethod = req.getMethod().toLowerCase();
    String requestPath = req.getPathInfo();
    Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);

    if (Objects.isNull(handler)) {
      return;
    }

    Map<String, Object> paramMap = new HashMap<>();
    Enumeration<String> paramNames =  req.getParameterNames();
    while (paramNames.hasMoreElements()) {
      String paramName = paramNames.nextElement();
      String paramValue = req.getParameter(paramName);
      paramMap.put(paramName, paramValue);
    }

    //TODO body

    Param param = new Param(paramMap);

    Object controllerBean = BeanHelper.getBean(handler.getControllerClass());

    Object result = ReflectionUtil.invokeMethod(controllerBean, handler.getActionMethod(), param);

    if (result instanceof View) {
      View view = (View)result;
      String path = view.getPath();
      if (StringUtils.isBlank(path)) {
        return;
      }

      if (StringUtils.startsWith(path, "/")) {
        resp.sendRedirect(req.getContextPath() + path);
      } else{
        Map<String, Object> model = view.getModel();
        model.forEach(req::setAttribute);
        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
      }

    } else if (result instanceof Data) {

    }
  }
}
