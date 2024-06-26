package com.realworld.seleniumrealworldapp;

import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.UserSettingsPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.TopBarPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LogoutTests extends BaseTest {
    @Value("${base.url}")
    private String baseUrl;
    @Autowired
    private UserSettingsPage userSettingsPage;
    @Autowired
    private TopBarPage topBarPage;

    @Test(groups = {"user"}, testName = "Should navigate to home page after logout")
    public void logout() {
        // Arrange - First we go to a page that requires authentication
        userSettingsPage.visit();
        topBarPage.performLogout();

        // Assert - Then we assert that we are redirected to the home page
        assertThat(driver.getCurrentUrl()).isEqualTo(baseUrl + "/");
        topBarPage.assertThatUserPicNoLongerExists();
    }
}
