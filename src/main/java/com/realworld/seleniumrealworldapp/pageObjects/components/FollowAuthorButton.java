package com.realworld.seleniumrealworldapp.pageObjects.components;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.pageObjects.BasePage;
import org.openqa.selenium.WebElement;

@PageObject
public class FollowAuthorButton extends BasePage {
    public void clickButton() {
        getButton().click();
    }

    public WebElement getButton() {
        return this.getElementsByTestId("follow-button").get(0);
    }
}
