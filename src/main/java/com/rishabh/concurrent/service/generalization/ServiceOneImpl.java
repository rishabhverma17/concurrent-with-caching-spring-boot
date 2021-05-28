package com.rishabh.concurrent.service.generalization;

import com.rishabh.concurrent.model.ServiceOneBO;
import com.rishabh.concurrent.service.abtraction.IServiceOne;
import org.springframework.stereotype.Service;

@Service
public class ServiceOneImpl implements IServiceOne {
    @Override
    public ServiceOneBO doTaskOne(Object...parameter) {
        return ServiceOneBO.builder()
                .serviceMessage("Response From Service 1").build();
    }
}
