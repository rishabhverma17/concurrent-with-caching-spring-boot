package com.rishabh.concurrent.service.generalization;

import com.rishabh.concurrent.service.abtraction.IndependentService1;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class IndependentServiceOneImpl implements IndependentService1 {
    @Override
    public CompletableFuture<String> getValueFromIDS1(Object...parameter) throws InterruptedException {
        Thread.sleep(2000);
        return CompletableFuture.completedFuture("Response from IDS 1");
    }
}
