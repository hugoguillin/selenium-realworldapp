package com.realworld.seleniumrealworldapp;

import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.utils.api.FavoritesApi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArticleDetailTests extends BaseTest {
    @Autowired
    private FavoritesApi favoritesApi;

    @Test
    @Tag("sanity")
    @Tag("articles")
    @DisplayName("Should like an article")
    public void testArticleDetail() {
        System.out.println(favoritesApi.getUserFavorites("cypress-user"));
        favoritesApi.unfavoriteArticle(0);
    }

    @Test
    public void testArticleDetail2() {
        System.out.println("Let's test 2");
    }
}
