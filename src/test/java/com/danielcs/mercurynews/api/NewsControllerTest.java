package com.danielcs.mercurynews.api;

import com.danielcs.mercurynews.models.ArticleDTO;
import com.danielcs.mercurynews.services.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class NewsControllerTest {

    private NewsController controller;
    private NewsService newsService;
    private final List<ArticleDTO> articles = Arrays.asList(
            new ArticleDTO("this", "is", "a", "tiring", "process", "to", "write"),
            new ArticleDTO("it", "is", "also", "tiring", "to", "just", "copy")
    );

    @BeforeEach
    void init() {
        newsService = mock(NewsService.class);
        controller = new NewsController(newsService);
    }

    @Test
    void getArticlesWithNoQueryParamsThrows() {
        ResponseEntity<?> resp = controller.getArticlesByCountryOrCategory(null, null, null);
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), resp);
    }

    @Test
    void getArticlesWithWrongCountryCodeThrows() {
        ResponseEntity<?> resp = controller.getArticlesByCountryOrCategory("kek", null, null);
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), resp);
    }

    @Test
    void getArticlesWithWrongCategoryThrows() {
        ResponseEntity<?> resp = controller.getArticlesByCountryOrCategory("gb", "random", null);
        assertEquals(new ResponseEntity<>(HttpStatus.BAD_REQUEST), resp);
    }

    @Test
    void getArticlesByCountryOrCategoryReturnsArticles() {
        when(newsService.fetchArticlesByCountryOrCategory("gb", "science", null))
                .thenReturn(articles);
        ResponseEntity<?> resp = controller.getArticlesByCountryOrCategory("gb", "science", null);
        ResponseEntity<?> expected = new ResponseEntity<>(articles, HttpStatus.OK);
        assertEquals(expected, resp);
    }

    @Test
    void getArticlesBySourcesReturnsArticles() {
        when(newsService.fetchArticlesBySources(new HashSet<>(Arrays.asList("cnn", "bbc")), null))
                .thenReturn(articles);
        List<ArticleDTO> resp = controller.getArticlesBySources("cnn,bbc", null);
        assertEquals(articles, resp);
    }
}