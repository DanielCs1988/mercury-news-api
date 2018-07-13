package com.danielcs.mercurynews.models;

import java.util.List;

public class NewsResultWrapper {

    private String status;
    private int totalResults;
    private List<ArticleSource> articles;

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<ArticleSource> getArticles() {
        return articles;
    }
}
