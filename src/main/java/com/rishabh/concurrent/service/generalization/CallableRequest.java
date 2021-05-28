package com.rishabh.concurrent.service.generalization;

import com.rishabh.concurrent.model.ConcurrentRequest;
import com.rishabh.concurrent.service.abtraction.MethodCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class CallableRequest<T> implements Callable<T> {

    Logger LOGGER = LoggerFactory.getLogger(CallableRequest.class);

    private MethodCache methodCache = new MethodCacheImpl();

    private final ConcurrentRequest request;
    private final ConcurrentHashMap<ConcurrentRequest, T> result;

    public CallableRequest(ConcurrentRequest request, ConcurrentHashMap<ConcurrentRequest, T> result) {
        this.request = request;
        this.result = result;
    }

    public CallableRequest(ConcurrentRequest request) {
        this.request = request;
        result = null;
    }

    @Override
    public T call() throws Exception {
        T response = null;
        try {
            boolean enabled = false;
            StopWatch watch = new StopWatch();
            watch.start();
            response = invokeMethod(request);
            watch.stop();
            if (enabled) {
                LOGGER.info("ConcurrentRequest for {} , and param {} executing in conc request executor , time {}", request.getMethodName(), request.getParameter(), watch.getTotalTimeMillis());
            }
            if (result != null && response != null)
                result.put(request, response);
        } catch (Exception e) {
            LOGGER.error("Error while executing concurrent tasks.", e);
        }
        return response;
    }

    private <T> T invokeMethod(ConcurrentRequest request) {
        if (request.getObject() != null) {
            T result = null;
            try {
                Method method = getAppropriateMethod(request.getMethodName(), request.getObject(), request.getParameter());
                request = formatArgs(request, method);
                method.setAccessible(true);
                Object response = method.invoke(request.getObject(), request.getParameter());
                if (response == null) {
                    return null;
                }
                result = (T) response;
                request.setSuccessful(true);
            } catch (Exception ex) {
                request.setException(ex);
                LOGGER.error(ex.getMessage(), ex);
            }
            return result;
        } else {
            return null;
        }
    }

    private Method getAppropriateMethod(String name, Object object, Object... params) throws NoSuchMethodException {
        List<Method> eligibleMethods = methodCache.getMethod(name, object, params);
        Method mte = null;
        for (Method method : eligibleMethods) {
            Class[] types = method.getParameterTypes();
            boolean foundMethod = true;
            if (method.isVarArgs()) {
                foundMethod = getAppropriateMethodIfVarArgs(types, params);
            } else {
                for (int i = 0; i < types.length; i++) {
                    Class<? extends Object> clazz = types[i];
                    if (params[i] != null && !clazz.isInstance(params[i])) {
                        foundMethod = false;
                        break;
                    }
                }
            }
            if (foundMethod) {
                mte = method;
                break;
            }
        }
        if (mte != null) {
            return mte;
        }
        throw new NoSuchMethodException(name + " and given arguments in : " + object.getClass());
    }

    private ConcurrentRequest formatArgs(ConcurrentRequest request, Method method) {
        if (method.isVarArgs()) {
            int varCount = method.getParameterTypes().length;
            int actualVarCount = request.getParameter().length;
            int varParmsCount = actualVarCount - varCount + 1;
            Object[] params = new Object[varCount];
            Object varParams = java.lang.reflect.Array.newInstance(method.getParameterTypes()[varCount - 1].getComponentType(), varParmsCount);
            System.arraycopy(request.getParameter(), 0, params, 0, varCount - 1);
            for (int j = 0; j < varParmsCount; j++) {
                Array.set(varParams, j, request.getParameter()[j + varCount - 1]);
            }
            params[varCount - 1] = varParams;
            return new ConcurrentRequest(request.getMethodName(), request.getObject(), params);
        }
        return request;
    }

    private boolean getAppropriateMethodIfVarArgs(Class[] types, Object... params) {
        for (int i = 0; i < types.length; i++) {
            Class clazz = types[i];
            if (i == types.length - 1) {
                clazz = types[i].getComponentType();
                for (int j = i; j < params.length; j++) {
                    if (params[j] != null && !clazz.isInstance(params[j])) {
                        return false;
                    }
                }
            } else {
                if (params[i] != null && !clazz.isInstance(params[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
