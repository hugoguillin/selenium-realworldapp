package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.AuthorDetailPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.ArticlesFeedPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.FollowAuthorButton;
import com.realworld.seleniumrealworldapp.utils.api.AuthorApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthorDetailTests extends BaseTest {
    @Autowired
    private AuthorDetailPage authorDetailPage;
    @Autowired
    private ArticlesFeedPage articlesFeedPage;
    @Autowired
    private FollowAuthorButton followAuthorButton;
    @Autowired
    private NetworkInterceptor networkInterceptor;
    @Autowired
    private AuthorApi authorApi;
    private int randomArticleIndex = 0;

    @BeforeClass
    public void setUpSuite() {
        super.setUpSuite();
        randomArticleIndex = (int) (Math.random() * 10);
    }

    @Test(groups = {"articles", "author"}, testName = "Should display author articles")
    public void displayAuthorArticles() {
        // Arrange
        var authorName = authorDetailPage.visit(randomArticleIndex);
        var articlesDisplayed = articlesFeedPage.getArticleTitles();
        List<String> authorArticles = JsonPath.parse(authorApi.getAuthorArticles(authorName)).read("$.articles[*].title");

        // Assert
        assertThat(articlesDisplayed).containsExactlyInAnyOrderElementsOf(authorArticles);
    }

    @Test(groups = {"articles", "author"}, testName = "Should display favorited articles")
    public void displayFavoritedArticles() throws IOException {
        // Arrange
        authorDetailPage.visit(randomArticleIndex);
        ReadContext ctx = JsonPath.parse(ResourceUtils.getFile("classpath:mockedFavoritedArticles.json"));
        var mockedFavoritedArticles = ctx.read("$");
        List<String> articlesTitles = ctx.read("$.articles[*].title");
        int numberOfArticles = ctx.read("$.articles.length()");
        authorDetailPage.goToFavoritedArticles(mockedFavoritedArticles);
        var articlesDisplayed = articlesFeedPage.getArticleTitles();

        // Assert
        assertThat(articlesDisplayed.size()).isEqualTo(numberOfArticles);
        assertThat(articlesDisplayed).containsExactlyInAnyOrderElementsOf(articlesTitles);
    }

    @Test(groups = {"articles", "author", "sanity"}, testName = "Should follow author")
    public void followAuthor() {
        // Arrange
        authorApi.unfollowAuthor(0);
        var authorName = authorDetailPage.visit(0);

        // Act
        networkInterceptor.interceptResponse(".*/profiles/" + authorName + "/follow", "POST");
        followAuthorButton.clickButton();
        boolean isFollowing =  JsonPath.parse(networkInterceptor.waitForResponse()).read("$.profile.following");


        // Assert
        assertThat(followAuthorButton.getButton().getText()).contains("Unfollow");
        assertThat(isFollowing).isTrue();
    }

}
