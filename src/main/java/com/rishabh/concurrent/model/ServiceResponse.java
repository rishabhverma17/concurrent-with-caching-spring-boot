package com.rishabh.concurrent.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceResponse {

    private Long id;
    private String serviceOneResponse;
    private String serviceTwoResponse;
    private String serviceThreeResponse;
    private String independentServiceOneResponse;
    private String independentServiceTwoResponse;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Service Response [");
        builder.append("Response for request id =").append(id).append(",");
        builder.append("Service One Response =").append(serviceOneResponse).append(",");
        builder.append("Service Two Response =").append(serviceTwoResponse).append(",");
        builder.append("Service Two Response =").append(serviceThreeResponse).append(",");
        builder.append("Independent Service One Response =").append(independentServiceOneResponse).append(",");
        builder.append("Independent Service Two Response =").append(independentServiceTwoResponse);
        builder.append("]");
        return builder.toString();
    }
}
