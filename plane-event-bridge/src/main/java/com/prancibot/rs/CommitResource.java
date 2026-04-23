package com.prancibot.rs;

import com.prancibot.dto.CommitSyncRequestDTO;
import com.prancibot.service.CommitSyncService;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("commit")
public class CommitResource {
    private final CommitSyncService commitSyncService;

    public CommitResource(CommitSyncService commitSyncService) {
        this.commitSyncService = commitSyncService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendCommit(CommitSyncRequestDTO commit) {
        try {
            int successCount = commitSyncService.syncCommit(commit);
            return Response.ok(Map.of("success", successCount)).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Map.of("errors", ex)).build();
        }
    }
}
