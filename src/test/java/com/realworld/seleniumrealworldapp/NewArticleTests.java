package com.realworld.seleniumrealworldapp;

import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.ArticleDetailPage;
import com.realworld.seleniumrealworldapp.pageObjects.NewArticlePage;
import com.realworld.seleniumrealworldapp.utils.Utils;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import com.realworld.seleniumrealworldapp.utils.entities.ArticleWrapper;
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
    @Autowired
    private ArticlesApi articlesApi;

    private ArticleWrapper newArticle;

    @BeforeEach
    public void setUp() {
        super.setUp();
        newArticle = Utils.generateNewArticleData(true);
    }
    @Test
    @Tag("sanity")
    @Tag("articles")
    @DisplayName("Should create a new article")
    public void testCreateNewArticle() {
        // Arrange
        String articleSlug = newArticle.getArticle().getTitle()
                .toLowerCase()
                .replaceAll("[.\\s]", "-");
        newArticlePage.visit();

        // Act
        networkInterceptor.interceptResponse(".*/api/articles", "POST");
        newArticlePage.createArticle(newArticle.getArticle());
        networkInterceptor.awaitRequestCompletion();

        // Assert
        assertThat(driver.getCurrentUrl()).contains(articleSlug);
        articleDetailPage.assertThatArticleDisplaysExpectedData(newArticle.getArticle());
    }

    @Test
    @Tag("sanity")
    @Tag("articles")
    @DisplayName("Should update an existing article")
    public void updateArticle() {
        // Arrange
        NewArticle updatedArticle = Utils.generateNewArticleData(false).getArticle();
        String newArticleSlug = newArticle.getArticle().getTitle()
                .toLowerCase()
                .replaceAll("[.\\s]", "-");
        String updatedArticleSlug = updatedArticle.getTitle()
                .toLowerCase()
                .replaceAll("[.\\s]", "-");
        articlesApi.createNewArticle(newArticle);
        articleDetailPage.goToArticle(newArticleSlug);
        articleDetailPage.goToEditArticle();

        // Act
        networkInterceptor.interceptResponse(".*/api/articles/"+ newArticleSlug + updatedArticleSlug, "GET");
        newArticlePage.editArticle(updatedArticle);
        networkInterceptor.awaitRequestCompletion();

        // Assert
        assertThat(driver.getCurrentUrl()).contains(updatedArticleSlug);
        assertThat(articleDetailPage.getArticleBodyText())
                .isEqualTo(newArticle.getArticle().getBody() + updatedArticle.getBody());

    }
}
