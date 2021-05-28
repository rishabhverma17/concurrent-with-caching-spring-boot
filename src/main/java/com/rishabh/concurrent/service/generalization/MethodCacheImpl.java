package com.rishabh.concurrent.service.generalization;

import com.rishabh.concurrent.service.abtraction.MethodCache;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MethodCacheImpl implements MethodCache {

    private final ConcurrentHashMap<String, List<Method>> methodCache = new ConcurrentHashMap<String, List<Method>>();

    @Override
    public List<Method> getMethod(String name, Object object, Object...params) {
        List<Method> methods = null;
        String key = new StringBuilder(name).append('-').append(object.hashCode()).append('-').append(params.length).toString();
        methods = methodCache.get(key);
        if(methods == null) {
            methods = Collections.EMPTY_LIST;
            Method[] mt = object.getClass().getDeclaredMethods();
            if(mt.length > 0) {
                methods = new ArrayList<>();
            }
            for (Method method : mt) {
                if(method.isVarArgs()){
                    if(name.equals(method.getName()) && (params.length >= method.getParameterTypes().length)){
                        methods.add(method);
                    }
                } else{
                    if (name.equals(method.getName()) && params.length == method.getParameterTypes().length) {
                        methods.add(method);
                    }
                }
            }
            methodCache.put(key, methods);
        }
        return methods;
    }
}
