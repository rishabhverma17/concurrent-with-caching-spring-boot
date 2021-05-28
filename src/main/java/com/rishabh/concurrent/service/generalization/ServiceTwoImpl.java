package com.rishabh.concurrent.service.generalization;

import com.rishabh.concurrent.model.ServiceTwoBO;
import com.rishabh.concurrent.service.abtraction.IServiceTwo;
import org.springframework.stereotype.Service;

@Service
public class ServiceTwoImpl implements IServiceTwo {
    @Override
    public ServiceTwoBO doTaskTwo(Object...parameter) throws InterruptedException {
        Thread.sleep(1000);
        return ServiceTwoBO.builder()
                .serviceMessage("Response From Service 2").build();
    }
}
