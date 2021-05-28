package com.rishabh.concurrent.service.abtraction;

import com.rishabh.concurrent.model.ServiceThreeBO;

public interface IServiceThree {
    ServiceThreeBO doTaskThree(Object...parameter) throws InterruptedException;
}
