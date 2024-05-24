package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import org.springframework.beans.factory.annotation.Autowired;

@PageObject
public class GlobalFeedPage extends BasePage{
    @Autowired
    private NetworkInterceptor networkInterceptor;

    public void goToGlobalFeed() {
        networkInterceptor.interceptResponse(".*/api/articles\\?.*", "GET");
        getByTestId("global-feed").click();
        networkInterceptor.awaitRequestCompletion();
    }
}
