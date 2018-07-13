package com.danielcs.mercurynews.models;

public class ArticleSource extends Article {

    private Source source;

    @Override
    public String getSource() {
        return source.getName();
    }
}
