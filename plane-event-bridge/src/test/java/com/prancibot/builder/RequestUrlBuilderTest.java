package com.prancibot.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestUrlBuilderTest {
    @Test
    public void testGetSpecificComment() {
        RequestUrlBuilder builder = new RequestUrlBuilder();
        String actual = builder
                .withWorkspace("abc")
                .withProject("cde")
                .withWorkItem("fgh")
                .specificComment("111")
                .build();

        assertEquals("/workspaces/abc/projects/cde/work-items/fgh/comments/111/", actual);
    }
}
