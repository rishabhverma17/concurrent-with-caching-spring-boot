package com.rishabh.concurrent.service.abtraction;

import java.util.concurrent.CompletableFuture;

public interface IndependentService2 {
    CompletableFuture<String> getValueFromIDS2(Object...parameter) throws InterruptedException;
}
