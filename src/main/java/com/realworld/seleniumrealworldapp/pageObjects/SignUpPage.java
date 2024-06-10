package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.entities.NewUser;
import org.openqa.selenium.Keys;
import org.springframework.beans.factory.annotation.Value;

@PageObject
public class SignUpPage extends BasePage {
    @Value("${base.url}")
    String baseUrl;

    public void visit() {
        getDriver().get(baseUrl + "/register");
    }

    public void registerNewUser(NewUser user) {
        this.getByTestId("username").sendKeys(user.username());
        this.getByTestId("email").sendKeys(user.email());
        this.getByTestId("password").sendKeys(user.password() + Keys.ENTER);
    }

    public void assertErrorMessageIsVisible(String message) {
        wait.until(driver -> this.getElementByText(message).isDisplayed());
    }
}
