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
import com.realworld.seleniumrealworldapp.utils.entities.UserLogin;
import com.realworld.seleniumrealworldapp.utils.entities.UserSettings;
import com.realworld.seleniumrealworldapp.utils.enums.UserSettingsFields;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

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

    @BeforeClass
    public void setUpSuite(ITestContext context) {
        super.setUpSuite(context);
    }

    @BeforeMethod
    public void setUp(Method m) {
        super.setUp(m);
        ((JavascriptExecutor)getDriver()).executeScript("localStorage.clear()");
        newUser = Utils.generateNewUserData();
        fieldsToUpdate = Utils.generateUserSettingsData();
        userApi.registerUser(newUser);
        loginPage.login(newUser.getUser().email(), newUser.getUser().password());
        userSettingsPage.visit();
    }

    @Test(groups = {"user"}, testName = "Should update user profile picture")
    public void updateProfilePicture() {
        // Act
        userSettingsPage.updateField(UserSettingsFields.IMAGE, fieldsToUpdate.image());
        userSettingsPage.submitForm();

        // Assert
        assertThat(topBarPage.getUserPic().getAttribute("src")).isEqualTo(fieldsToUpdate.image());
    }

    @Test(groups = {"user"}, testName = "Should update username")
    public void updateUsername() {
        // Act
        networkInterceptor.interceptResponse(".*/api/user", "PUT");
        userSettingsPage.updateField(UserSettingsFields.USERNAME, fieldsToUpdate.username());
        userSettingsPage.submitForm();
        var response = networkInterceptor.waitForResponse();
        var username = JsonPath.parse(response).read("$.user.username");

        // Assert
        assertThat(username).isEqualTo(fieldsToUpdate.username());
        assertThat(topBarPage.getUsername()).isEqualTo(fieldsToUpdate.username());
    }

    @Test(groups = {"user"}, testName = "Should update user bio")
    public void updateBio() {
        // Act
        networkInterceptor.interceptResponse(".*/api/user", "PUT");
        userSettingsPage.updateField(UserSettingsFields.BIO, fieldsToUpdate.bio());
        userSettingsPage.submitForm();
        var response = networkInterceptor.waitForResponse();
        var bio = JsonPath.parse(response).read("$.user.bio");

        // Assert
        assertThat(bio).isEqualTo(fieldsToUpdate.bio());
        assertThat(userSettingsPage.getField(UserSettingsFields.BIO).getAttribute("value"))
                .isEqualTo(fieldsToUpdate.bio());
    }

    @Test(groups = {"user", "sanity"}, testName = "Should update user email")
    public void updateEmail() {
        // Arrange
        UserLogin user = new UserLogin(fieldsToUpdate.email(), newUser.getUser().password());

        // Act
        networkInterceptor.interceptResponse(".*/api/user", "PUT");
        userSettingsPage.updateField(UserSettingsFields.EMAIL, user.email());
        userSettingsPage.submitForm();
        networkInterceptor.waitForResponse();
        var emailUpdated = JsonPath.parse(userApi.getUser(user)).read("$.user.email");

        // Assert
        assertThat(emailUpdated).isEqualTo(user.email());
        assertThat(userSettingsPage.getField(UserSettingsFields.EMAIL).getAttribute("value"))
                .isEqualTo(user.email());
    }

    @Test(groups = {"user", "sanity"}, testName = "Should update user password")
    public void updatePassword() {
        // Arrange
        UserLogin user = new UserLogin(newUser.getUser().email(), fieldsToUpdate.password());

        // Act
        networkInterceptor.interceptResponse(".*/api/user", "PUT");
        userSettingsPage.updateField(UserSettingsFields.PASSWORD, user.password());
        userSettingsPage.submitForm();
        networkInterceptor.waitForResponse();
        // An authenticated request (with the new pass) is made to fetch the user info
        // If it fails, the password was not updated
        var emailUpdated = JsonPath.parse(userApi.getUser(user)).read("$.user.email");

        // Assert
        assertThat(emailUpdated).isEqualTo(user.email());
        // If this assertion works ok in a real application, it would be a security risk
        assertThat(userSettingsPage.getField(UserSettingsFields.PASSWORD).getAttribute("value"))
                .isEqualTo(user.password());

    }

    @Test(groups = {"user"}, testName = "Should update all fields at once")
    public void updateAllFields() {
        // Act
        networkInterceptor.interceptResponse(".*/api/user", "PUT");
        userSettingsPage.updateAllFields(fieldsToUpdate);
        networkInterceptor.waitForResponse();

        // Assert
        assertThat(topBarPage.getUserPic().getAttribute("src")).isEqualTo(fieldsToUpdate.image());
        assertThat(topBarPage.getUsername()).isEqualTo(fieldsToUpdate.username());
        assertThat(userSettingsPage.getField(UserSettingsFields.BIO).getAttribute("value"))
                .isEqualTo(fieldsToUpdate.bio());
        assertThat(userSettingsPage.getField(UserSettingsFields.EMAIL).getAttribute("value"))
                .isEqualTo(fieldsToUpdate.email());
        assertThat(userSettingsPage.getField(UserSettingsFields.PASSWORD).getAttribute("value"))
                .isEqualTo(fieldsToUpdate.password());

    }
}
