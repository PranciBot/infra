package com.dthvinh.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WorkItemDTO {

    private String id;
    private String name;

    @JsonProperty("description_stripped")
    private String description;

    @JsonProperty("sequence_id")
    private int sequenceId;

    private String priority;

    private List<AssigneeDTO> assignees;

    private List<Object> labels;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;
}
