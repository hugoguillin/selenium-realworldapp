package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.SignUpPage;
import com.realworld.seleniumrealworldapp.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SignUpTests extends BaseTest {
    @Autowired
    private SignUpPage signUpPage;
    @Autowired
    private NetworkInterceptor networkInterceptor;

    @BeforeEach
    public void setUp() {
        super.setUp();
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.localStorage.clear();");
    }

    @Test
    @Tag("user")
    @DisplayName("Should register valid new user")
    public void registerValidNewUser() {
        // Arrange
        var newUser = Utils.generateNewUserData();
        signUpPage.visit();

        // Act
        networkInterceptor.interceptResponse(".*/api/user", "GET");
        signUpPage.registerNewUser(newUser);
        String registeredEmail = JsonPath.parse(networkInterceptor.waitForResponse()).read("$.user.email");

        // Assert
        assertThat(registeredEmail).isEqualTo(newUser.email());
        assertThat(signUpPage.getUsernameFromTopBar()).isEqualTo(newUser.username());
    }
}
