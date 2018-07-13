package com.danielcs.mercurynews.services;

import com.danielcs.mercurynews.models.ArticleDTO;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private HttpRequest http;

    public NewsService(HttpRequest http) {
        this.http = http;
    }

    public List<ArticleDTO> fetchArticlesByCountryOrCategory(String country, String category, String keyword) {
        Map<String, String> queryParams = new HashMap<>();
        if (country != null) {
            queryParams.put("country", country);
        }
        if (category != null) {
            queryParams.put("category", category);
        }
        if (keyword != null) {
            queryParams.put("q", keyword);
        }
        return http.fetchArticles(queryParams);
    }

    public List<ArticleDTO> fetchArticlesBySources(Set<String> sources, String keyword) {
        String sourceParam = sources.stream().collect(Collectors.joining(","));
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("sources", sourceParam);
        if (keyword != null) {
            queryParams.put("q", keyword);
        }
        return http.fetchArticles(queryParams);
    }
}
