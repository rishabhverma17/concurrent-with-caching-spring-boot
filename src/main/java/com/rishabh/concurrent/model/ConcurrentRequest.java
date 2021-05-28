package com.rishabh.concurrent.model;

import lombok.Data;

@Data
public class ConcurrentRequest {
    private String methodName;
    private Object object;
    private Object[] parameter;
    private boolean successful;
    private Throwable exception;

    public ConcurrentRequest(String methodName, Object object, Object...parameter) {
        this.methodName = methodName;
        this.object = object;
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ConcurrentRequest [");
        builder.append("methodName=").append(methodName);
        builder.append("]");
        return builder.toString();
    }
}
