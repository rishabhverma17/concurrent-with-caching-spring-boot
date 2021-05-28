package com.rishabh.concurrent.service.core;

import com.rishabh.concurrent.aspect.LogExecutionTime;
import com.rishabh.concurrent.model.*;
import com.rishabh.concurrent.service.abtraction.*;
import com.rishabh.concurrent.model.ConcurrentRequest;
import com.rishabh.concurrent.utils.ExecutorUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class GetResultOfAllTask {

    Logger LOGGER = LoggerFactory.getLogger(GetResultOfAllTask.class);

    @Autowired
    IServiceOne serviceOne;
    @Autowired
    IServiceTwo serviceTwo;
    @Autowired
    IServiceThree serviceThree;
    @Autowired
    IndependentService1 independentService1;
    @Autowired
    IndependentService2 independentService2;
    @Autowired
    ExecutorUtil executorUtil;

    @Autowired
    private CacheManager cacheManager;

    @SneakyThrows
    @LogExecutionTime
    //@Cacheable(cacheNames="ServiceResponseCache", key="#p0")
    public ServiceResponse runAllTask(Long userId){

        ServiceResponse serviceResponse = null;

        CompletableFuture<String> IDS1Response = independentService1.getValueFromIDS1();

        CompletableFuture<String> IDS2Response = independentService2.getValueFromIDS2();

        List<ConcurrentRequest> parallelReq = new ArrayList<>();
        ConcurrentRequest serviceOneConcurrentRequest = new ConcurrentRequest("doTaskOne", serviceOne, "ServiceOne");
        parallelReq.add(serviceOneConcurrentRequest);

        ConcurrentRequest serviceTwoConcurrentRequest = new ConcurrentRequest("doTaskTwo", serviceTwo, "ServiceTwo");
        parallelReq.add(serviceTwoConcurrentRequest);

        ConcurrentRequest serviceThreeConcurrentRequest = new ConcurrentRequest("doTaskThree", serviceThree, "ServiceThree");
        parallelReq.add(serviceThreeConcurrentRequest);

        ConcurrentRequest IDS1ConcurrentRequest = new ConcurrentRequest("getValueFromIDS1", independentService1, "independentService1");

        ConcurrentRequest IDS2ConcurrentRequest = new ConcurrentRequest("getValueFromIDS2", independentService2, "independentService2");

        Cache cache = cacheManager.getCache("ServiceResponseCache");

        try {
            Map<ConcurrentRequest, Object> concurrentResponse = executorUtil.executeConcurrently(parallelReq, true);
            concurrentResponse.put(IDS1ConcurrentRequest,IDS1Response.join());
            concurrentResponse.put(IDS2ConcurrentRequest,IDS2Response.join());
            serviceResponse = populateResponse(concurrentResponse);
            serviceResponse.setId(userId);

            /** Putting the value in Cache */
            cache.put(serviceResponse.getId(), serviceResponse);

        } catch (ClassCastException e) {
            LOGGER.error("Class cast exception occurred in ConcurrentResponse {}", e);
        } catch (Exception e) {
            LOGGER.error("Exception occurred in ConcurrentResponse {}", e);
        }

        return serviceResponse;
    }

    private ServiceResponse populateResponse(Map<ConcurrentRequest, Object> concurrentResponse) {
        String serviceOneResponse = null;
        String serviceTwoResponse = null;
        String serviceThreeResponse = null;
        String IDS1Response = null;
        String IDS2Response = null;
        if (concurrentResponse != null && !concurrentResponse.isEmpty()) {
            for (Map.Entry<ConcurrentRequest, Object> entry : concurrentResponse.entrySet()) {

                if (entry.getValue() instanceof ServiceOneBO) {
                    serviceOneResponse = ((ServiceOneBO) entry.getValue()).getServiceMessage();
                }
                if (entry.getValue() instanceof ServiceTwoBO) {
                    serviceTwoResponse = ((ServiceTwoBO) entry.getValue()).getServiceMessage();
                }
                if (entry.getValue() instanceof ServiceThreeBO) {
                    serviceThreeResponse = ((ServiceThreeBO) entry.getValue()).getServiceMessage();
                }
                if (entry.getValue() instanceof String ) {
                    if(((String) entry.getValue()).equalsIgnoreCase("Response from IDS 1")){
                        IDS1Response = (String) entry.getValue();
                    }else if(((String) entry.getValue()).equalsIgnoreCase("Response from Independent Service 2")){
                        IDS2Response = (String) entry.getValue();
                    }
                }
            }
        }
        return ServiceResponse.builder()
                .serviceOneResponse(serviceOneResponse)
                .serviceTwoResponse(serviceTwoResponse)
                .serviceThreeResponse(serviceThreeResponse)
                .independentServiceOneResponse(IDS1Response)
                .independentServiceTwoResponse(IDS2Response)
                .build();
    }
}
