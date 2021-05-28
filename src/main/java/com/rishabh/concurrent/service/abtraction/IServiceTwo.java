package com.rishabh.concurrent.service.abtraction;

import com.rishabh.concurrent.model.ServiceTwoBO;

public interface IServiceTwo {
    ServiceTwoBO doTaskTwo(Object...parameter) throws InterruptedException;
}
