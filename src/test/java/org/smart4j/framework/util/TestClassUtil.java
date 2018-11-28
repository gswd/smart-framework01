package org.smart4j.framework.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestClassUtil {

  @Test
  public void getClassSetTest() {
    String packageName = "org.apache.log4j";
    Set<Class<?>> classSet = ClassUtil.getClassSet(packageName);
    classSet.forEach(System.out :: println);
    Assertions.assertTrue(classSet.size() > 0);

  }

  @Test
  public void fileTest() {
    Path path = Paths.get("e:/IdeaProjects/smart-framework01");

    try {
      Files.walk(path).forEach(System.out::println);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void loadClassTest() {
    String className = "org.smart4j.framework.util.CastUtil";
    ClassUtil.loadClass(className, false);
  }
}
