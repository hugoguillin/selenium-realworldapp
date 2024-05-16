package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;

@PageObject
public class GlobalFeedPage extends BasePage{
    public int getAmountOfLikes(int articleIndex) {
        wait.until(driver -> this.getElementsByTestId("fav-button").size() > articleIndex);
        var favButtonText = this.getElementsByTestId("fav-button").get(articleIndex).getText();
        var likes = favButtonText.replaceAll("\\D+", "");
        return Integer.parseInt(likes);
    }
}
