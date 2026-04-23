package com.prancibot.service.impl;

import com.prancibot.builder.RequestUrlBuilder;
import com.prancibot.common.logging.AppLogger;
import com.prancibot.config.PlaneConfig;
import com.prancibot.dto.CommitSyncRequestDTO;
import com.prancibot.dto.CreateCommentResponseDTO;
import com.prancibot.dto.WorkItemDTO;
import com.prancibot.formatter.CommitFormatter;
import com.prancibot.proxy.PlaneAPIProxy;
import com.prancibot.proxy.dto.CreateCommentDTO;
import com.prancibot.service.CommitSyncService;
import io.smallrye.mutiny.tuples.Tuple2;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.context.ManagedExecutor;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ApplicationScoped
public class CommitSyncServiceImpl implements CommitSyncService {
    private final PlaneAPIProxy planeAPIProxy;
    private final AppLogger LOGGER = AppLogger.getLogger(getClass());
    private final PlaneConfig config;
    private final ManagedExecutor executor;

    public CommitSyncServiceImpl(PlaneAPIProxy planeAPIProxy, PlaneConfig config, ManagedExecutor executor) {
        this.planeAPIProxy = planeAPIProxy;
        this.config = config;
        this.executor = executor;
    }

    public int syncCommit(CommitSyncRequestDTO commit) {
        LOGGER.info("Start syncing commit");
        long start = System.currentTimeMillis();
        int success = 0;

        Map<String, WorkItemDTO> commitMap = collectCommitWorkItemMap(commit.getMessage());
        for (Map.Entry<String, WorkItemDTO> entry : commitMap.entrySet()) {
            String issueKey = entry.getKey();
            WorkItemDTO workItem = entry.getValue();

            String requestUrl = new RequestUrlBuilder().withWorkspace(config.workspace())
                    .withProject(config.projectInternalId()).withWorkItem(workItem.getId())
                    .allComments().build();
            CreateCommentDTO createCommentDTO = new CreateCommentDTO();
            createCommentDTO.setCommentHtml(CommitFormatter.htmlCommit(commit));
            createCommentDTO.setAccess(commit.getAccess());
            createCommentDTO.setCommentJson(Map.of("a", "a"));

            try {
                CreateCommentResponseDTO responseDTO = planeAPIProxy.sendPost(requestUrl, createCommentDTO, CreateCommentResponseDTO.class);
                LOGGER.info("Successfully sync commit for issue {} at {}", issueKey, responseDTO.getCreatedAt());
                success += 1;
            } catch (Exception e) {
                LOGGER.error("Failed to sync for issue {} , work item {}", issueKey, workItem, e);
            }
        }

        LOGGER.info("Successfully sync {}/{} commits in {} seconds", success, commitMap.size(), System.currentTimeMillis() - start);
        return success;
    }

    private Set<String> getIssueKeysFromCommitMessage(String commitMessage) {
        Pattern pattern = Pattern.compile("[A-Z]+-\\d+");
        Matcher matcher = pattern.matcher(commitMessage);

        Set<String> results = new HashSet<>();

        while (matcher.find()) {
            results.add(matcher.group());
        }

        return results;
    }

    private Map<String, WorkItemDTO> collectCommitWorkItemMap(String commitMessage) {
        ConcurrentHashMap<String, WorkItemDTO> commitWorkItemMap = new ConcurrentHashMap<>();
        Set<String> issueKeys = getIssueKeysFromCommitMessage(commitMessage);

        List<CompletableFuture<Tuple2<String, Boolean>>> collectResultTasks = issueKeys.stream()
                .map(key -> CompletableFuture.supplyAsync(() -> collectIssue(key, commitWorkItemMap), executor)).toList();

        // Block until all the jobs above done running.
        CompletableFuture<Void> waitAllTask = CompletableFuture.allOf(collectResultTasks.toArray(new CompletableFuture[0]));
        List<Tuple2<String, Boolean>> allResult = waitAllTask.thenApply(rs ->
                collectResultTasks.stream()
                        .map(CompletableFuture::join)
                        .toList()).join();

        LOGGER.info("Found {}/{} work items match commit message, not found: {}",
                allResult.stream().filter(Tuple2::getItem2).toList().size(), issueKeys.size(), allResult.stream().filter(e -> !e.getItem2()).map(Tuple2::getItem1).toList());

        return commitWorkItemMap;
    }

    private Tuple2<String, Boolean> collectIssue(String key,
                                                 Map<String, WorkItemDTO> commitWorkItemMap) {
        String requestUrl = new RequestUrlBuilder()
                .withWorkspace(config.workspace()).withWorkItem().specificIssue(key).build();
        try {
            WorkItemDTO workItem = planeAPIProxy.sendGet(requestUrl, WorkItemDTO.class);
            if (Objects.nonNull(workItem)) {
                commitWorkItemMap.put(key, workItem);
                return Tuple2.of(key, true);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to process issue {}", key, e);
        }
        return Tuple2.of(key, false);
    }
}
