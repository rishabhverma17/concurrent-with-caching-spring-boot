package com.rishabh.concurrent.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceTwoBO {
    private String serviceMessage;
}