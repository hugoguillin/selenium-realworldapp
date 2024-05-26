package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.UserDetailsPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.ArticlesFeedPage;
import com.realworld.seleniumrealworldapp.utils.Utils;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import com.realworld.seleniumrealworldapp.utils.api.AuthorApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDetailsTests extends BaseTest {
    @Value("${user.username}")
    private String username;
    @Autowired
    private UserDetailsPage userDetailsPage;
    @Autowired
    private ArticlesFeedPage articlesFeedPage;
    @Autowired
    private ArticlesApi articlesApi;
    @Autowired
    private AuthorApi authorApi;
    @Autowired
    private NetworkInterceptor networkInterceptor;

    @Test
    @Tag("user")
    @DisplayName("Should navigate to settings page")
    public void shouldNavigateToSettingsPage() {
        // Arrange
        userDetailsPage.visit();

        // Assert
        assertThat(userDetailsPage.getSettingsButton().getAttribute("href"))
                .isEqualTo(baseUrl + "/settings");
    }

    @Test
    @Tag("user")
    @DisplayName("Should display expected user articles")
    public void displayExpectedUserArticles() {
        // Arrange - Let's make sure user has at least 5 articles
        for (int i = 0; i < 5; i++) {
            var newArticle = Utils.generateNewArticleData(true);
            articlesApi.createNewArticle(newArticle);
        }
        userDetailsPage.visit();
        List<String> userArticles = JsonPath.parse(authorApi.getAuthorArticles(username)).read("$.articles[*].title");
        userArticles = userArticles.subList(0, Math.min(userArticles.size(), 10));
        List<String> displayedArticles = articlesFeedPage.getArticleTitles();

        // Assert
        assertThat(userDetailsPage.getMyArticlesTab().getAttribute("class")).contains("active");
        assertThat(displayedArticles).containsExactlyElementsOf(userArticles);
    }

    @Test
    @Tag("user")
    @DisplayName("Should display user favorited articles")
    public void displayFavoritedArticles() {
        // Arrange
        List<String> expectedTitles = userDetailsPage.setUpArticlesFavorited();
        userDetailsPage.visit();

        // Act
        networkInterceptor.interceptResponse(".*/articles\\?favorited=.*", "GET");
        userDetailsPage.goToFavoritedArticles();
        networkInterceptor.waitForResponse();

        // Assert
        List<String> displayedFavorites = articlesFeedPage.getArticleTitles();
        assertThat(displayedFavorites).containsExactlyElementsOf(expectedTitles);
    }
}
