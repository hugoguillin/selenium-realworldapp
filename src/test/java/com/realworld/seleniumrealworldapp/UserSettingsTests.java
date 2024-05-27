package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.UserSettingsPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.LoginPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.TopBarPage;
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
    @Autowired
    private TopBarPage topBarPage;
    @Autowired
    private NetworkInterceptor networkInterceptor;

    private NewUserWrapper newUser;
    private UserSettings fieldsToUpdate;

    @BeforeEach
    public void beforeEach() {
        super.setUp();
        ((JavascriptExecutor)driver).executeScript("localStorage.clear()");
        newUser = Utils.generateNewUserData();
        fieldsToUpdate = Utils.generateUserSettingsData();
        userApi.registerUser(newUser);
        loginPage.login(newUser.getUser().email(), newUser.getUser().password());
        userSettingsPage.visit();
    }

    @Test
    @Tag("user")
    @DisplayName("Should update user profile picture")
    public void updateProfilePicture() {
        // Act
        userSettingsPage.updateField(UserSettingsFields.IMAGE, fieldsToUpdate.image());

        // Assert
        assertThat(topBarPage.getUserPic().getAttribute("src")).isEqualTo(fieldsToUpdate.image());
    }

    @Test
    @Tag("user")
    @DisplayName("Should update username")
    public void updateUsername() {
        // Act
        networkInterceptor.interceptResponse(".*/api/user", "PUT");
        userSettingsPage.updateField(UserSettingsFields.USERNAME, fieldsToUpdate.username());
        var response = networkInterceptor.waitForResponse();
        var username = JsonPath.parse(response).read("$.user.username");

        // Assert
        assertThat(username).isEqualTo(fieldsToUpdate.username());
        assertThat(topBarPage.getUsername()).isEqualTo(fieldsToUpdate.username());
    }

    @Test
    @Tag("user")
    @DisplayName("Should update user bio")
    public void updateBio() {
        // Act
        networkInterceptor.interceptResponse(".*/api/user", "PUT");
        userSettingsPage.updateField(UserSettingsFields.BIO, fieldsToUpdate.bio());
        var response = networkInterceptor.waitForResponse();
        var bio = JsonPath.parse(response).read("$.user.bio");

        // Assert
        assertThat(bio).isEqualTo(fieldsToUpdate.bio());
        assertThat(userSettingsPage.getField(UserSettingsFields.BIO).getAttribute("value"))
                .isEqualTo(fieldsToUpdate.bio());
    }

}
