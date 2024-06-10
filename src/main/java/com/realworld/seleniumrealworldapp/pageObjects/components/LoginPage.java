package com.realworld.seleniumrealworldapp.pageObjects.components;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.pageObjects.BasePage;
import org.springframework.beans.factory.annotation.Value;

@PageObject
public class LoginPage extends BasePage {
    @Value("${base.url}")
    private String baseUrl;

    public void login(String email, String password) {
        getDriver().get(baseUrl + "/login");
        getByTestId("email").sendKeys(email);
        getByTestId("password").sendKeys(password);
        getByTestId("login-button").click();
        wait.until(d -> getElementByText("Your Feed").isDisplayed());
    }
}
