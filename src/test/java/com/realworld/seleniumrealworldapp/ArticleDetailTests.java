package com.realworld.seleniumrealworldapp;

import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.ArticleDetailPage;
import com.realworld.seleniumrealworldapp.pageObjects.GlobalFeedPage;
import com.realworld.seleniumrealworldapp.utils.api.FavoritesApi;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArticleDetailTests extends BaseTest {
    @Autowired
    private FavoritesApi favoritesApi;
    @Autowired
    private GlobalFeedPage globalFeedPage;
    @Autowired
    private ArticleDetailPage articleDetailPage;

    @Test
    @Tag("sanity")
    @Tag("articles")
    @DisplayName("Should like an article")
    public void testArticleDetail() {
        // Arrange
        int articleIndex = 0;
        favoritesApi.unfavoriteArticle(articleIndex);
        articleDetailPage.visit(articleIndex);
        var likesBefore = globalFeedPage.getAmountOfLikes(articleIndex);

        // Act
        articleDetailPage.giveLikeToAnArticle(articleIndex);

        // Assert
        var likesAfter = globalFeedPage.getAmountOfLikes(articleIndex);
        Assertions.assertThat(likesAfter).isEqualTo(likesBefore + 1);
    }

    @Test
    public void testArticleDetail2() {
        System.out.println("Let's test 2");
    }
}
