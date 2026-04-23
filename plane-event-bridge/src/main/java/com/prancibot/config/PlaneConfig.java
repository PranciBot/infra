package com.prancibot.config;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(
        prefix = "plane"
)
public interface PlaneConfig {
    String apiUrl();

    String apiKey();

    String workspace();

    String projectId();

    String projectInternalId();
}

