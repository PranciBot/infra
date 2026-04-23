package com.prancibot.proxy.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prancibot.config.PlaneConfig;
import com.prancibot.proxy.PlaneAPIProxy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@ApplicationScoped
public class PlaneAPIProxyImpl implements PlaneAPIProxy {
    private final HttpClient client;
    private final PlaneConfig config;
    private final ObjectMapper mapper;

    public PlaneAPIProxyImpl(PlaneConfig config) {
        this.config = config;
        client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();
        mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
    }

    public String sendGet(String url) throws IOException, InterruptedException {
        HttpRequest request = createBaseRequestBuilder(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    @Override
    public <T> T sendGet(String url, Class<T> target) throws IOException, InterruptedException {
        HttpRequest request = createBaseRequestBuilder(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponseToType(response, target);
    }

    public String sendPost(String url, Object body) throws IOException, InterruptedException {
        String strBody = mapper.writeValueAsString(body);
        HttpRequest request = createBaseRequestBuilder(url)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(strBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String sendPost(String url, String body) throws IOException, InterruptedException {
        HttpRequest request = createBaseRequestBuilder(url)
                .header("Content-Type", MediaType.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    @Override
    public <T> T sendPost(String url, String body, Class<T> target) throws IOException, InterruptedException {
        HttpRequest request = createBaseRequestBuilder(url)
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponseToType(response, target);
    }

    @Override
    public <T> T sendPost(String url, Object body, Class<T> target) throws IOException, InterruptedException {
        String strBody = mapper.writeValueAsString(body);
        HttpRequest request = createBaseRequestBuilder(url)
                .header("Content-Type", "application/json; charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(strBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return handleResponseToType(response, target);
    }

    private HttpRequest.Builder createBaseRequestBuilder(String url) {
        if (config.apiUrl().isEmpty()
                || config.apiKey().isEmpty()) {
            throw new IllegalArgumentException("API URL and API KEY need to be configured!!!");
        }

        return HttpRequest.newBuilder(URI.create(config.apiUrl() + url))
                .header("X-API-KEY", config.apiKey());
    }

    private <T> T handleResponseToType(HttpResponse<String> response, Class<T> target) throws JsonProcessingException {
        int status = response.statusCode();
        String responseBody = response.body();


        if (status < 200 || status >= 300) {
            throw new RuntimeException("HTTP " + status + " error: " + responseBody);
        }

        if (responseBody == null || responseBody.isBlank()) {
            return null;
        }

        return mapper.readValue(responseBody, target);
    }
}
