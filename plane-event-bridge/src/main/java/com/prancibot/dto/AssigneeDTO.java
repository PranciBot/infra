package com.prancibot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AssigneeDTO {

    private String id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;

    private String avatar;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("display_name")
    private String displayName;
}
