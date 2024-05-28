package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.entities.UserSettings;
import com.realworld.seleniumrealworldapp.utils.enums.UserSettingsFields;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Value;

@PageObject
public class UserSettingsPage extends BasePage {
    @Value("${base.url}")
    private String baseUrl;

    public void visit() {
        driver.get(baseUrl + "/settings");
        wait.until(d -> getElementByText("Your Settings").isDisplayed());
    }

    public WebElement getField(UserSettingsFields fieldName) {
        return this.getByTestId(fieldName.getField());
    }

    public void updateField(UserSettingsFields fieldName, String value) {
        WebElement field = this.getField(fieldName);
        field.clear();
        field.sendKeys(value);
    }

    public void updateAllFields(UserSettings userSettings) {
        this.updateField(UserSettingsFields.IMAGE, userSettings.image());
        this.updateField(UserSettingsFields.USERNAME, userSettings.username());
        this.updateField(UserSettingsFields.BIO, userSettings.bio());
        this.updateField(UserSettingsFields.EMAIL, userSettings.email());
        this.updateField(UserSettingsFields.PASSWORD, userSettings.password());
        this.submitForm();
    }

    public void submitForm() {
        getElementByText("Update Settings").click();
    }

}
