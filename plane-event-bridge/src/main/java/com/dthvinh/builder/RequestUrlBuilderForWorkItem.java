package com.dthvinh.builder;

public class RequestUrlBuilderForWorkItem extends RequestUrlBuilder {

    public RequestUrlBuilderForWorkItem(StringBuilder sb) {
        super(sb);
    }

    public RequestUrlBuilder allComments() {
        appendSegment("comments");
        return this;
    }

    public RequestUrlBuilder specificComment(String id) {
        appendSegment("comments");
        appendSegment(id);
        return this;
    }

    public RequestUrlBuilder specificIssue(String issueKey) {
        appendSegment(issueKey);
        return this;
    }
}
