package com.rishabh.concurrent.service.generalization;

import com.rishabh.concurrent.service.abtraction.IndependentService2;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class IndependentServiceTwoImpl implements IndependentService2 {
    @Override
    public CompletableFuture<String> getValueFromIDS2(Object...parameter) throws InterruptedException {
        Thread.sleep(1000);
        return CompletableFuture.completedFuture("Response from Independent Service 2");
    }
}
