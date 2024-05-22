package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.GlobalFeedPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.ArticlesFeedPage;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class GlobalFeedTests extends BaseTest {
    @Autowired
    private GlobalFeedPage globalFeedPage;
    @Autowired
    private ArticlesFeedPage articlesFeedPage;
    @Autowired
    private ArticlesApi articlesApi;

    @Test
    @Tag("articles")
    @DisplayName("Should display expected articles")
    public void displayExpectedArticles() {
        // Arrange
        globalFeedPage.goToGlobalFeed();
        var displayedArticles = articlesFeedPage.getArticleTitles();
        List<String> articlesFromApi = JsonPath.parse(articlesApi.getArticles(10)).read("$.articles[*].title");
        assertThat(displayedArticles).containsExactlyInAnyOrderElementsOf(articlesFromApi);
    }
}
