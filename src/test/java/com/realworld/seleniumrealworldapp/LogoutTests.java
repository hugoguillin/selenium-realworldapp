package com.realworld.seleniumrealworldapp;

import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.UserSettingsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class LogoutTests extends BaseTest {
    @Value("${base.url}")
    private String baseUrl;
    @Autowired
    private UserSettingsPage userSettingsPage;

    @Test
    @Tag("user")
    @DisplayName("Should navigate to home page after logout")
    public void logout() {
        // Arrange
        userSettingsPage.visit();
        userSettingsPage.performLogout();

        // Assert
        assertThat(driver.getCurrentUrl()).isEqualTo(baseUrl + "/");
        userSettingsPage.assertThatUserPicNoLongerExists();
    }
}
