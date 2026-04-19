package com.dthvinh.service;

import com.dthvinh.dto.CommitSyncRequestDTO;

public interface CommitSyncService {
    int syncCommit(CommitSyncRequestDTO commit);
}
