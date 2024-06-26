package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.GlobalFeedPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.ArticlesFeedPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.FavoritesPage;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import com.realworld.seleniumrealworldapp.utils.api.FavoritesApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GlobalFeedTests extends BaseTest {
    @Autowired
    private GlobalFeedPage globalFeedPage;
    @Autowired
    private ArticlesFeedPage articlesFeedPage;
    @Autowired
    private FavoritesPage favoritesPage;
    @Autowired
    private ArticlesApi articlesApi;
    @Autowired
    private FavoritesApi favoritesApi;

    @Test(groups = {"articles"}, testName = "Should display expected articles")
    public void displayExpectedArticles() {
        // Arrange
        globalFeedPage.goToGlobalFeed();
        var displayedArticles = articlesFeedPage.getArticleTitles();
        List<String> articlesFromApi = JsonPath.parse(articlesApi.getArticles(10)).read("$.articles[*].title");
        assertThat(displayedArticles).containsExactlyInAnyOrderElementsOf(articlesFromApi);
    }

    @Test(groups = {"articles"}, testName = "Should like an article")
    public void likeAnArticle() {
        // Arrange
        int articleIndex = 0;
        favoritesApi.unfavoriteArticle(articleIndex);
        globalFeedPage.goToGlobalFeed();
        var likesBefore = favoritesPage.getAmountOfLikes(articleIndex);

        // Act
        favoritesPage.likeAnArticle(articleIndex);

        // Assert
        var likesAfter = favoritesPage.getAmountOfLikes(articleIndex);
        assertThat(likesAfter).isEqualTo(likesBefore + 1);
    }
}
