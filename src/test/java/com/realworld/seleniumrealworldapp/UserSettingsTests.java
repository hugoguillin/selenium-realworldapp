package com.realworld.seleniumrealworldapp;

import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.UserSettingsPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.LoginPage;
import com.realworld.seleniumrealworldapp.utils.Utils;
import com.realworld.seleniumrealworldapp.utils.api.UserApi;
import com.realworld.seleniumrealworldapp.utils.entities.NewUserWrapper;
import com.realworld.seleniumrealworldapp.utils.entities.UserSettings;
import com.realworld.seleniumrealworldapp.utils.enums.UserSettingsFields;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserSettingsTests extends BaseTest {
    @Autowired
    private UserSettingsPage userSettingsPage;
    @Autowired
    private UserApi userApi;
    @Autowired
    private LoginPage loginPage;
    private NewUserWrapper newUser;
    private UserSettings userSettings;

    @BeforeEach
    public void beforeEach() {
        super.setUp();
        ((JavascriptExecutor)driver).executeScript("localStorage.clear()");
        newUser = Utils.generateNewUserData();
        userSettings = Utils.generateUserSettingsData();
        userApi.registerUser(newUser);
        loginPage.login(newUser.getUser().email(), newUser.getUser().password());
        userSettingsPage.visit();
    }

    @Test
    @Tag("user")
    @DisplayName("Should update user profile picture")
    public void updateProfilePicture() {
        // Act
        userSettingsPage.updateField(UserSettingsFields.IMAGE.getField(), userSettings.image());
        userSettingsPage.submitForm();

        // Assert
        assertThat(userSettingsPage.getUserPic().getAttribute("src")).isEqualTo(userSettings.image());
    }

}
