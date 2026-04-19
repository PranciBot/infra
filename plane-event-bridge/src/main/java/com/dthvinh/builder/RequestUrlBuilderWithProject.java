package com.dthvinh.builder;

public class RequestUrlBuilderWithProject extends RequestUrlBuilder {

    public RequestUrlBuilderWithProject(StringBuilder sb) {
        super(sb);
    }

    public RequestUrlBuilderForWorkItem withWorkItem(String workItem) {
        appendSegment("work-items");
        appendSegment(workItem);
        return new RequestUrlBuilderForWorkItem(sb);
    }
}