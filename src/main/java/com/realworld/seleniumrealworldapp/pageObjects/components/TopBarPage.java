package com.realworld.seleniumrealworldapp.pageObjects.components;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.pageObjects.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

@PageObject
public class TopBarPage extends BasePage {
    public String getUsername() {
        return this.getByTestId("user-pic").findElement(By.xpath("./..")).getText();
    }

    public WebElement getUserPic() {
        return this.getByTestId("user-pic");
    }


    public void assertThatUserPicNoLongerExists() {
        assertThat(driver.findElements(By.cssSelector("[data-testid='user-pic']"))).isEmpty();
    }

    public void performLogout() {
        this.getUserPic().click();
        this.getByTestId("logout").click();
    }
}
