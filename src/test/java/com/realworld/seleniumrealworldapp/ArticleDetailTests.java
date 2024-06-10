package com.realworld.seleniumrealworldapp;

import com.github.javafaker.Faker;
import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.ArticleDetailPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.FavoritesPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.FollowAuthorButton;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import com.realworld.seleniumrealworldapp.utils.api.AuthorApi;
import com.realworld.seleniumrealworldapp.utils.api.FavoritesApi;
import com.realworld.seleniumrealworldapp.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleDetailTests extends BaseTest {
    @Autowired
    private FavoritesApi favoritesApi;
    @Autowired
    private ArticleDetailPage articleDetailPage;
    @Autowired
    private AuthorApi authorApi;
    @Autowired
    private FollowAuthorButton followAuthorButton;
    @Autowired
    private FavoritesPage favoritesPage;
    @Autowired
    private ArticlesApi articlesApi;

    @Test(groups = {"sanity", "articles"}, testName = "Should like an article")
    public void likeArticle() {
        // Arrange
        int articleIndex = 0;
        favoritesApi.unfavoriteArticle(articleIndex);
        articleDetailPage.visit(articleIndex);
        var likesBefore = favoritesPage.getAmountOfLikes(articleIndex);

        // Act
        favoritesPage.likeAnArticle(articleIndex);

        // Assert
        var likesAfter = favoritesPage.getAmountOfLikes(articleIndex);
        assertThat(likesAfter).isEqualTo(likesBefore + 1);
    }

    @Test(groups = {"articles"}, testName = "Should follow an author")
    public void followAuthor() {
        // Arrange
        int articleIndex = 0;
        authorApi.unfollowAuthor(articleIndex);
        articleDetailPage.visit(articleIndex);

        // Act
        followAuthorButton.clickButton();

        // Assert
        assertThat(followAuthorButton.getButton().getText()).contains("Unfollow");
        followAuthorButton.clickButton();
        assertThat(followAuthorButton.getButton().getText()).contains("Follow");
    }

    @Test(groups = {"sanity", "articles"}, testName = "Should add a comment to an article")
    public void addComment() {
        // Arrange
        int articleIndex = 0;
        String message = Faker.instance().chuckNorris().fact();
        articlesApi.deleteArticleComments(articleIndex);
        articleDetailPage.visit(articleIndex);

        // Act
        articleDetailPage.sendComment(message);

        // Assert
        articleDetailPage.assertCommentIsVisible(message);
        articleDetailPage.assertCommentHasTestUserUsername(message);
    }

    @Test(groups = {"sanity", "comments"}, testName = "Should delete a comment from an article")
    public void deleteComment() {
        // Arrange
        int articleIndex = 0;
        String message = Faker.instance().chuckNorris().fact();
        articlesApi.addCommentToArticle(articleIndex, message);
        articleDetailPage.visit(articleIndex);

        // Act
        articleDetailPage.deleteComment(message);

        // Assert
        articleDetailPage.assertCommentIsNotVisible(message);
    }

    @Test(groups = {"sanity", "articles"}, testName = "Should delete an article", enabled = false)
    public void deleteArticle() {
        // Arrange
        var newArticle = Utils.generateNewArticleData(false);
        String slug = JsonPath.parse(articlesApi.createNewArticle(newArticle)).read("$.article.slug");
        articleDetailPage.goToArticle(slug);

        // Act
        articleDetailPage.deleteArticle();

        // Assert
        articleDetailPage.assertAppNavigatesToHomePageAfterArticleDeletion();
        articleDetailPage.assertThatNavigatingToDeletedArticleReturns404(slug);
    }
}
