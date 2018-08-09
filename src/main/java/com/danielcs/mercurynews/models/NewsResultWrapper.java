package com.danielcs.mercurynews.models;

import java.util.List;

public class NewsResultWrapper {

    private String status;
    private int totalResults;
    private List<ArticleSource> articles;

    public NewsResultWrapper(String status, int totalResults, List<ArticleSource> articles) {
        this.status = status;
        this.totalResults = totalResults;
        this.articles = articles;
    }

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<ArticleSource> getArticles() {
        return articles;
    }

    @Override
    public String toString() {
        return "NewsResultWrapper{" +
                "status='" + status + '\'' +
                ", totalResults=" + totalResults +
                ", articles=" + articles +
                '}';
    }
}
