package com.realworld.seleniumrealworldapp.pageObjects.components;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.pageObjects.BasePage;

@PageObject
public class FavoritesPage extends BasePage {
    public void likeAnArticle(int articleIndex) {
        wait.until(driver -> this.getElementsByTestId("fav-button").get(articleIndex).isDisplayed());
        this.getElementsByTestId("fav-button").get(articleIndex).click();
    }

    public int getAmountOfLikes(int articleIndex) {
        wait.until(driver -> this.getElementsByTestId("fav-button").size() > articleIndex);
        var favButtonText = this.getElementsByTestId("fav-button").get(articleIndex).getText();
        var likes = favButtonText.replaceAll("\\D+", "");
        return Integer.parseInt(likes);
    }
}
