package com.prancibot.proxy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class CreateCommentDTO {
    @JsonProperty("comment_json")
    private Map<String, Object> commentJson;

    @JsonProperty("comment_html")
    private String commentHtml;

    private String access;

    @JsonProperty("external_source")
    private String externalSource;

    @JsonProperty("external_id")
    private String externalId;

    private String parent;
}
