package com.rishabh.concurrent.service.abtraction;

import java.lang.reflect.Method;
import java.util.List;

public interface MethodCache {
    List<Method> getMethod(String name, Object object, Object...params);
}
