package com.rishabh.concurrent.service.generalization;

import com.rishabh.concurrent.model.ServiceThreeBO;
import com.rishabh.concurrent.service.abtraction.IServiceThree;
import org.springframework.stereotype.Service;

@Service
public class ServiceThreeImpl implements IServiceThree {
    @Override
    public ServiceThreeBO doTaskThree(Object...parameter) throws InterruptedException {
        Thread.sleep(1000);
        return ServiceThreeBO.builder()
                .serviceMessage("Response From Service 3").build();
    }
}
