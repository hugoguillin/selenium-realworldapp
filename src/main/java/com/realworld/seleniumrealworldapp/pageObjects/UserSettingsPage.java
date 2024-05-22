package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;

@PageObject
public class UserSettingsPage extends BasePage {
    @Value("${base.url}")
    private String baseUrl;

    public void visit() {
        driver.get(baseUrl + "/settings");
    }

    public WebElement getUserPic() {
        return this.getByTestId("user-pic");
    }

    public By getUserPicLocator() {
        return By.cssSelector("[data-testid='user-pic']");
    }

    public void performLogout() {
        this.getUserPic().click();
        this.getByTestId("logout").click();
    }
}
