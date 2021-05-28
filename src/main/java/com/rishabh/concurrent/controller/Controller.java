package com.rishabh.concurrent.controller;

import com.rishabh.concurrent.model.ServiceResponse;
import com.rishabh.concurrent.service.core.GetResultOfAllTask;
import com.rishabh.concurrent.utils.ExecutorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v1/")
public class Controller {

    @Autowired
    private GetResultOfAllTask getResultOfAllTask;

    @Autowired
    private CacheManager cacheManager;

    @GetMapping("/execute/{userId}")
    public CompletableFuture<ServiceResponse> Execute(@PathVariable Long userId) {
        return CompletableFuture.supplyAsync(() ->
                getResultOfAllTask.runAllTask(userId)
        ,ExecutorUtil.getExecutor());

        /*executorUtil.getExecutor().submit(() -> {
               processLog.process(logModel, logstashBaseUrl);
             });*/
        //return getResultOfAllTask.runAllTask(userId);
    }

    @GetMapping("/getFromCache/{userId}")
    public CompletableFuture<ServiceResponse> getValueFromCache(@PathVariable Long userId) {
        Cache cache = cacheManager.getCache("ServiceResponseCache");
        ServiceResponse serviceResponse = cache.get(userId,ServiceResponse.class);
        if(serviceResponse != null){
            return CompletableFuture.completedFuture(serviceResponse);
        }else {
            return CompletableFuture.supplyAsync(() ->
                            getResultOfAllTask.runAllTask(userId)
                    ,ExecutorUtil.getExecutor());
        }
    }

    @GetMapping("/clear")
    public String clearCache() {
        Cache cache = cacheManager.getCache("ServiceResponseCache");
        cache.clear();
        return "Task Execution Success";
    }
}
