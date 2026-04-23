package com.prancibot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommitSyncRequestDTO {

    private String commitHash;
    private String message;
    private String author;
    private String branch;
    private String repository;
    private String issueKey;
    private long timestamp;
    private String access;
    // The source that use this endpoint except git
    private String source = "git";

    // The id generated for this commit from that source
    private String sourceId;
}