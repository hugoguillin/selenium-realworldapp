package com.realworld.seleniumrealworldapp.pageObjects;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import com.realworld.seleniumrealworldapp.utils.api.FavoritesApi;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;

@PageObject
public class UserDetailsPage extends BasePage {
    @Value("${base.url}")
    private String baseUrl;
    @Value("${user.username}")
    private String username;
    @Autowired
    private FavoritesApi favoritesApi;
    @Autowired
    private ArticlesApi articlesApi;

    public void visit() {
        driver.get(baseUrl + "/profile/" + username);
    }

    public void goToFavoritedArticles() {
        getElementByText("Favorited Articles").click();
    }

    public WebElement getSettingsButton() {
        return getByTestId("edit-profile-settings");
    }

    public WebElement getMyArticlesTab() {
        return getElementByText("My Articles");
    }

    public List<String> setUpArticlesFavorited() {
        // Let's make test totally deterministic. First, let's unfavorite all articles
        List<String> userFavorites = JsonPath.parse(favoritesApi.getUserFavorites(username)).read("$.articles[*].slug");
        for (var slug : userFavorites) {
            favoritesApi.unfavoriteArticle(slug);
        }

        // Now, let's favorite 5 random articles
        int randomIndex = new Random().nextInt(45);
        ReadContext articles = JsonPath.parse(articlesApi.getArticles(50));
        List<String> titles = articles.read("$.articles[" + randomIndex + ":" + (randomIndex+5) +"].title");
        List<String> slugs = articles.read("$.articles[" + randomIndex + ":" + (randomIndex+5) +"].slug");

        for (var slug : slugs) {
            favoritesApi.favoriteArticle(slug);
        }
        return titles;
    }
}
