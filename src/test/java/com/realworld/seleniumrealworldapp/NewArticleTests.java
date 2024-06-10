package com.realworld.seleniumrealworldapp;

import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.ArticleDetailPage;
import com.realworld.seleniumrealworldapp.pageObjects.NewArticlePage;
import com.realworld.seleniumrealworldapp.utils.Utils;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import com.realworld.seleniumrealworldapp.utils.entities.ArticleWrapper;
import com.realworld.seleniumrealworldapp.utils.entities.NewArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

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

    @BeforeMethod
    public void setUp(Method m) {
        super.setUp(m);
        newArticle = Utils.generateNewArticleData(true);
    }

    @Test(groups = {"sanity", "articles"}, testName = "Should create a new article")
    public void testCreateNewArticle() {
        // Arrange
        String articleSlug = newArticle.getArticle().getTitle()
                .toLowerCase()
                .replaceAll("[.\\s]", "-");
        newArticlePage.visit();

        // Act
        networkInterceptor.interceptResponse(".*/api/articles", "POST");
        newArticlePage.createArticle(newArticle.getArticle());
        networkInterceptor.waitForResponse();

        // Assert
        assertThat(driver.getCurrentUrl()).contains(articleSlug);
        articleDetailPage.assertThatArticleDisplaysExpectedData(newArticle.getArticle());
    }

    @Test(groups = {"sanity", "articles"}, testName = "Should update an existing article")
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
        networkInterceptor.waitForResponse();

        // Assert
        assertThat(driver.getCurrentUrl()).contains(updatedArticleSlug);
        assertThat(articleDetailPage.getArticleBodyText())
                .isEqualTo(newArticle.getArticle().getBody() + updatedArticle.getBody());

    }
}
