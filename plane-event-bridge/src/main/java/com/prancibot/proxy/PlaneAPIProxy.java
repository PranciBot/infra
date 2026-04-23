package com.prancibot.proxy;

import java.io.IOException;

public interface PlaneAPIProxy {
    String sendGet(String url) throws IOException, InterruptedException;

    <T> T sendGet(String url, Class<T> target) throws IOException, InterruptedException;

    String sendPost(String url, Object body) throws IOException, InterruptedException;

    String sendPost(String url, String body) throws IOException, InterruptedException;

    <T> T sendPost(String url, String body, Class<T> target) throws IOException, InterruptedException;

    <T> T sendPost(String url, Object body, Class<T> target) throws IOException, InterruptedException;
}
