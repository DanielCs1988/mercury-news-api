package com.danielcs.mercurynews.models;

public class ArticleSource extends Article {

    private Source source;

    public ArticleSource(String author, String title, String description, String url, String urlToImage, String publishedAt, Source source) {
        super(author, title, description, url, urlToImage, publishedAt);
        this.source = source;
    }

    @Override
    public String getSource() {
        return source.getName();
    }
}
