package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.entities.UserSettings;
import com.realworld.seleniumrealworldapp.utils.enums.UserSettingsFields;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;

import static org.assertj.core.api.Assertions.assertThat;

@PageObject
public class UserSettingsPage extends BasePage {
    @Value("${base.url}")
    private String baseUrl;

    public void visit() {
        driver.get(baseUrl + "/settings");
        wait.until(d -> getElementByText("Your Settings").isDisplayed());
    }

    public WebElement getUserPic() {
        return this.getByTestId("user-pic");
    }

    public WebElement getField(String fieldName) {
        return this.getByTestId(fieldName);
    }

    public void updateField(String fieldName, String value) {
        WebElement field = this.getField(fieldName);
        field.clear();
        field.sendKeys(value);
    }

    public void updateAllFields(UserSettings userSettings) {
        this.updateField(UserSettingsFields.USERNAME.getField(), userSettings.username());
        this.updateField(UserSettingsFields.BIO.getField(), userSettings.bio());
        this.updateField(UserSettingsFields.EMAIL.getField(), userSettings.email());
        this.updateField(UserSettingsFields.PASSWORD.getField(), userSettings.password());
        this.updateField(UserSettingsFields.IMAGE.getField(), userSettings.image());
        this.submitForm();
    }

    public void submitForm() {
        getElementByText("Update Settings").click();
    }

    public void assertThatUserPicNoLongerExists() {
        assertThat(driver.findElements(By.cssSelector("[data-testid='user-pic']"))).isEmpty();
    }

    public void performLogout() {
        this.getUserPic().click();
        this.getByTestId("logout").click();
    }
}
