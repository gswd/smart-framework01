package org.smart4j.framework.util;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ClassUtil {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);

  public static ClassLoader getClassLoader() {
    return Thread.currentThread().getContextClassLoader();
  }

  /**
   *
   * @param className 类全名
   * @param isInitialized 指定是否执行类的初始化代码块，如果设置为false可以提高加载性能
   */
  public static Class<?> loadClass(String className, boolean isInitialized) {

    try {
      return Class.forName(className, isInitialized, getClassLoader());
    } catch (ClassNotFoundException e) {
      LOGGER.error("load class failure", e);
      throw new RuntimeException(e);
    }
  }

  public static Set<Class<?>> getClassSet(String packageName) {
    Set<Class<?>> classSet = new HashSet<>();

    String packageRootPath = StringUtils.replace(packageName, ".", "/");

    try {
      Enumeration<URL> urls = getClassLoader().getResources(packageRootPath);
      while (urls.hasMoreElements()) {
        URL url = urls.nextElement();
        String protocol = url.getProtocol();
        if (StringUtils.equals("file", protocol)) {

          addClass(classSet, url.toURI(), packageName);

        } else if (StringUtils.equals("jar", protocol)) {
          JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
          if (Objects.isNull(jarURLConnection)) {
            continue;
          }

          JarFile jarFile = jarURLConnection.getJarFile();
          if (Objects.isNull(jarFile)) {
            continue;
          }

          Enumeration<JarEntry> jarEntries = jarFile.entries();
          while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();
            if (jarEntryName.endsWith(".class")) {
              String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
              doAddClass(classSet, className);
            }
          }
        }

      }
    } catch (Exception e) {
      LOGGER.error("get class set failure", e);
      e.printStackTrace();
     }

    return classSet;
  }

  private static void addClass(Set<Class<?>> classSet, URI packageUri, String packageName) {
    Path path = Paths.get(packageUri);

    try {
      Files.list(path).filter(p -> Files.isDirectory(p)).forEach(dir -> {
        String subPackageName = packageName + "." + dir.getFileName().toString();
        URI subPackageUri = Paths.get(path.toString(), dir.getFileName().toString()).toUri();
        addClass(classSet, subPackageUri, subPackageName);
      });

      Files.list(path).filter(p -> !Files.isDirectory(p) && p.getFileName().toString().endsWith(".class")).forEach(c -> {
        String fileName = c.getFileName().toString();
        String className = fileName.substring(0, fileName.lastIndexOf("."));

        doAddClass(classSet, packageName + "." + className);
      });
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private static void doAddClass(Set<Class<?>> classSet, String className) {
    Class<?> clazz = loadClass(className, false);
    classSet.add(clazz);
  }
}
