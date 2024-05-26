package com.realworld.seleniumrealworldapp;

import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.UserDetailsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserDetailsTests extends BaseTest {
    @Autowired
    private UserDetailsPage userDetailsPage;

    @Test
    @Tag("user")
    @DisplayName("Should navigate to settings page")
    public void shouldNavigateToSettingsPage() {
        // Arrange
        userDetailsPage.visit();

        // Assert
        assertThat(userDetailsPage.getSettingsButton().getAttribute("href"))
                .isEqualTo(baseUrl + "/settings");
    }
}
