package com.prancibot.builder;

public class RequestUrlBuilder implements BaseBuilder {

    protected final StringBuilder sb;

    public RequestUrlBuilder() {
        this.sb = new StringBuilder();
    }

    protected RequestUrlBuilder(StringBuilder sb) {
        this.sb = sb;
    }

    protected void appendSegment(String value) {
        sb.append("/").append(value);
    }

    @Override
    public String build() {
        String url = sb.toString();
        if (!url.endsWith("/")) {
            url = url + "/";
        }
        return url;
    }

    public RequestUrlBuilderWithWorkspace withWorkspace(String workspace) {
        appendSegment("workspaces");
        appendSegment(workspace);
        return new RequestUrlBuilderWithWorkspace(sb);
    }
}
