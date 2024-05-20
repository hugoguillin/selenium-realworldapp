package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.AuthorDetailPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.ArticlesFeedPage;
import com.realworld.seleniumrealworldapp.utils.api.AuthorApi;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class AuthorDetailTests extends BaseTest {
    @Autowired
    private AuthorDetailPage authorDetailPage;
    @Autowired
    private ArticlesFeedPage articlesFeedPage;
    @Autowired
    private AuthorApi authorApi;


    @Test
    @Tag("articles")
    @Tag("author")
    public void displayAuthorArticles() {
        // Arrange
        var authorName = authorDetailPage.visit();
        var articlesDisplayed = articlesFeedPage.getArticleTitles();
        List<String> authorArticles = JsonPath.parse(authorApi.getAuthorArticles(authorName)).read("$.articles[*].title");

        // Assert
        assertThat(articlesDisplayed).containsExactlyInAnyOrderElementsOf(authorArticles);
    }
}
