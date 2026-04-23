package com.prancibot.service;

import com.prancibot.dto.CommitSyncRequestDTO;

public interface CommitSyncService {
    int syncCommit(CommitSyncRequestDTO commit);
}
