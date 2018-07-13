package com.danielcs.mercurynews.models;

public class ArticleDTO extends Article {

    private String source;

    public ArticleDTO(String author, String title, String description, String url, String urlToImage, String publishedAt, String source) {
        super(author, title, description, url, urlToImage, publishedAt);
        this.source = source;
    }

    public ArticleDTO(ArticleSource source) {
        this(
                source.getAuthor(), source.getTitle(), source.getDescription(), source.getUrl(),
                source.getUrlToImage(), source.getPublishedAt(), source.getSource()
        );
    }

    @Override
    public String getSource() {
        return source;
    }
}
