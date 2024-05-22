package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import org.springframework.beans.factory.annotation.Autowired;

@PageObject
public class GlobalFeedPage extends BasePage{
    @Autowired
    private NetworkInterceptor networkInterceptor;

    public int getAmountOfLikes(int articleIndex) {
        wait.until(driver -> this.getElementsByTestId("fav-button").size() > articleIndex);
        var favButtonText = this.getElementsByTestId("fav-button").get(articleIndex).getText();
        var likes = favButtonText.replaceAll("\\D+", "");
        return Integer.parseInt(likes);
    }

    public void goToGlobalFeed() {
        networkInterceptor.interceptResponse(".*/api/articles\\?.*");
        getByTestId("global-feed").click();
        networkInterceptor.awaitRequestCompletion();
    }
}
