package com.danielcs.mercurynews.services;

import com.danielcs.mercurynews.httputils.HttpRequestAssembler;
import com.danielcs.mercurynews.models.NewsResultWrapper;

import java.util.Map;

@HttpRequestAssembler
public interface HttpRequestSender {
    NewsResultWrapper getJsonWithQuery(String url, Map<String, String> queryParams);
    String getWithQuery(String url, Map<String, String> queryParams);
    String get(String url);
}
