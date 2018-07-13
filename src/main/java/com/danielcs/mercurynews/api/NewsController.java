package com.danielcs.mercurynews.api;

import com.danielcs.mercurynews.models.ArticleDTO;
import com.danielcs.mercurynews.services.NewsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/articles")
public class NewsController {

    private static final Set<String> ALLOWED_COUNTRY_CODES = new HashSet<>(Arrays.asList(
            "ae", "ar", "at", "au", "be", "bg", "br", "ca", "ch", "cn", "co", "cu", "cz", "de", "eg", "fr", "gb", "gr",
            "hk", "hu", "id", "ie", "il", "in", "it", "jp", "kr", "lt", "lv", "ma", "mx", "my", "ng", "nl", "no", "nz",
            "ph", "pl", "pt", "ro", "rs", "ru", "sa", "se", "sg", "si", "sk", "th", "tr", "tw", "ua", "us", "ve", "za"
    ));

    private static final Set<String> ALLOWED_CATEGORIES = new HashSet<>(Arrays.asList(
            "business", "entertainment", "general", "health", "science", "sports", "technology"
    ));

    private NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ArticleDTO>> getArticlesByCountryOrCategory(
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        if (
                (country == null && category == null && keyword == null) ||
                (country != null && !ALLOWED_COUNTRY_CODES.contains(country.toLowerCase())) ||
                (category != null && !ALLOWED_CATEGORIES.contains(category.toLowerCase()))
        ) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<ArticleDTO> news = newsService.fetchArticlesByCountryOrCategory(country, category, keyword);
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    @GetMapping("/by-source")
    public List<ArticleDTO> getArticlesBySources(
            @RequestParam(name = "sources") String sources,
            @RequestParam(name = "keyword", required = false) String keyword
    ) {
        Set<String> src = new HashSet<>(Arrays.asList(sources.split(",")));
        return this.newsService.fetchArticlesBySources(src, keyword);
    }

}
