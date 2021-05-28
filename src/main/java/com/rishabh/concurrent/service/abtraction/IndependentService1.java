package com.rishabh.concurrent.service.abtraction;

import java.util.concurrent.CompletableFuture;

public interface IndependentService1 {
    CompletableFuture<String> getValueFromIDS1(Object...parameter) throws InterruptedException;
}
