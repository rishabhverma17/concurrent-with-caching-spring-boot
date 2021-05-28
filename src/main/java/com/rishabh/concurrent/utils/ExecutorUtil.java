package com.rishabh.concurrent.utils;

import com.rishabh.concurrent.service.generalization.CallableRequest;
import com.rishabh.concurrent.model.ConcurrentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutorUtil {

    Logger LOGGER = LoggerFactory.getLogger(ExecutorUtil.class);

    private static ExecutorService executor = null;
    private static final long TIMEOUT = 2000;

    static {
        int threadCount = 10;
        executor = Executors.newFixedThreadPool(threadCount);
    }

    public static ExecutorService getExecutor() {
        return executor;
    }

    public <T> Map<ConcurrentRequest, T> executeConcurrently(List<ConcurrentRequest> requests, boolean overloaded){
        ConcurrentHashMap<ConcurrentRequest, T> result = new ConcurrentHashMap<>();

        if (CollectionUtils.isEmpty(requests)) {
            return result;
        }

        List<CallableRequest<T>> callables = new ArrayList<>(requests.size());
        for (ConcurrentRequest concurrentRequest : requests) {
            callables.add(new CallableRequest<>(concurrentRequest, result));
        }

        try {
            executor.invokeAll(callables, TIMEOUT, TimeUnit.MILLISECONDS);
            //executor.invokeAll(callables);
        } catch (InterruptedException e) {
            LOGGER.error("Thread interrupted while waiting for other threads to complete", e);
        }
        return result;
    }
}