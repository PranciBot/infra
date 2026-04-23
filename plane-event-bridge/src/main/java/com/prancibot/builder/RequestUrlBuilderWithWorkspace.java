package com.prancibot.builder;

public class RequestUrlBuilderWithWorkspace extends RequestUrlBuilder {

    public RequestUrlBuilderWithWorkspace(StringBuilder sb) {
        super(sb);
    }

    public RequestUrlBuilderWithProject withProject(String project) {
        appendSegment("projects");
        appendSegment(project);
        return new RequestUrlBuilderWithProject(sb);
    }

    public RequestUrlBuilderForWorkItem withWorkItem() {
        appendSegment("work-items");
        return new RequestUrlBuilderForWorkItem(sb);
    }
}