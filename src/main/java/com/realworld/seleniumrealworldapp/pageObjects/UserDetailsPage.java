package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;

@PageObject
public class UserDetailsPage extends BasePage {
    @Value("${base.url}")
    private String baseUrl;
    @Value("${user.username}")
    private String username;

    public void visit() {
        driver.get(baseUrl + "/profile/" + username);
    }

    public WebElement getSettingsButton() {
        return getByTestId("edit-profile-settings");
    }
}
