package com.realworld.seleniumrealworldapp;

import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.ArticleDetailPage;
import com.realworld.seleniumrealworldapp.pageObjects.NewArticlePage;
import com.realworld.seleniumrealworldapp.utils.Utils;
import com.realworld.seleniumrealworldapp.utils.entities.NewArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NewArticleTests extends BaseTest {
    @Autowired
    private NewArticlePage newArticlePage;
    @Autowired
    private ArticleDetailPage articleDetailPage;
    @Autowired
    private NetworkInterceptor networkInterceptor;

    private NewArticle newArticle;

    @BeforeEach
    public void setUp() {
        super.setUp();
        newArticle = Utils.generateNewArticleData(true).getArticle();
    }
    @Test
    @Tag("sanity")
    @Tag("articles")
    @DisplayName("Should create a new article")
    public void testCreateNewArticle() {
        // Arrange
        String articleSlug = newArticle.getTitle()
                .toLowerCase()
                .replaceAll("[.\\s]", "-");
        newArticlePage.visit();

        // Act
        networkInterceptor.interceptResponse(".*/api/articles", "POST");
        newArticlePage.createArticle(newArticle);
        newArticlePage.publishArticle();
        networkInterceptor.awaitRequestCompletion();

        // Assert
        assertThat(driver.getCurrentUrl()).contains(articleSlug);
        articleDetailPage.assertThatArticleDisplaysExpectedData(newArticle);
    }
}
