package com.prancibot.formatter;

import com.prancibot.dto.CommitSyncRequestDTO;

public class CommitFormatter {
    public static String htmlCommit(CommitSyncRequestDTO commit) {

        StringBuilder sb = new StringBuilder();

        sb.append("<div>");
        sb.append("<b>Commit:</b> ").append(commit.getCommitHash()).append("<br/>");
        sb.append("<b>Message:</b> ").append(escapeHtml(commit.getMessage())).append("<br/>");

        if (commit.getAuthor() != null) {
            sb.append("<b>Author:</b> ").append(escapeHtml(commit.getAuthor())).append("<br/>");
        }

        if (commit.getBranch() != null) {
            sb.append("<b>Branch:</b> ").append(escapeHtml(commit.getBranch())).append("<br/>");
        }

        if (commit.getRepository() != null) {
            sb.append("<b>Repo:</b> ").append(escapeHtml(commit.getRepository())).append("<br/>");
        }

        if (commit.getIssueKey() != null) {
            sb.append("<b>Issue:</b> ").append(commit.getIssueKey()).append("<br/>");
        }

        if (commit.getTimestamp() > 0) {
            sb.append("<b>Time:</b> ").append(new java.util.Date(commit.getTimestamp())).append("<br/>");
        }

        if (commit.getSource() != null) {
            sb.append("<b>Source:</b> ").append(escapeHtml(commit.getSource())).append("<br/>");
            if (commit.getSourceId() != null &&
                    !commit.getSourceId().equals(commit.getCommitHash())) {
                sb.append("<b>Source ID:</b> ")
                        .append(escapeHtml(commit.getSourceId()))
                        .append("<br/>");
            }
        }

        sb.append("</div>");
        return sb.toString();
    }

    private static String escapeHtml(String input) {
        if (input == null) return "";
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
