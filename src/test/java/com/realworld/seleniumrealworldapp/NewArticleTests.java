package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.ArticleDetailPage;
import com.realworld.seleniumrealworldapp.pageObjects.NewArticlePage;
import com.realworld.seleniumrealworldapp.utils.common.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NewArticleTests extends BaseTest {
    @Autowired
    private NewArticlePage newArticlePage;
    @Autowired
    private ArticleDetailPage articleDetailPage;
    @Autowired
    private NetworkInterceptor networkInterceptor;

    private Map<String, Object> newArticle;

    @BeforeEach
    public void setUp() {
        super.setUp();
        newArticle = JsonPath.parse(Utils.generateNewArticleData(true)).read("$.article");
    }
    @Test
    @Tag("sanity")
    @Tag("articles")
    @DisplayName("Should create a new article")
    public void testCreateNewArticle() {
        // Arrange
        String articleSlug = newArticle.get("title")
                .toString()
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
