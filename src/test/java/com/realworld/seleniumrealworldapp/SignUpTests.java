package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.SignUpPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.TopBarPage;
import com.realworld.seleniumrealworldapp.utils.Utils;
import com.realworld.seleniumrealworldapp.utils.entities.NewUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SignUpTests extends BaseTest {
    @Value("${user.email}")
    private String testUserEmail;
    @Autowired
    private SignUpPage signUpPage;
    @Autowired
    private TopBarPage topBarPage;
    @Autowired
    private NetworkInterceptor networkInterceptor;

    @BeforeClass
    public void setUpSuite() {
        System.out.println("Skip main setUpSuite");
    }

    @Test(groups = {"user"}, testName = "Should register valid new user")
    public void registerValidNewUser() {
        // Arrange
        var newUser = Utils.generateNewUserData().getUser();
        signUpPage.visit();

        // Act
        networkInterceptor.interceptResponse(".*/api/user", "GET");
        signUpPage.registerNewUser(newUser);
        String registeredEmail = JsonPath.parse(networkInterceptor.waitForResponse()).read("$.user.email");

        // Assert
        assertThat(registeredEmail).isEqualTo(newUser.email());
        assertThat(topBarPage.getUsername()).isEqualTo(newUser.username());
    }

    @Test(groups = {"user"}, testName = "Should display error message when registering with invalid email")
    public void registerWithExistingEmail() {
        // Arrange
        var newUser = new NewUser("test", testUserEmail, "test1234");
        signUpPage.visit();

        // Act
        signUpPage.registerNewUser(newUser);

        // Assert
        signUpPage.assertErrorMessageIsVisible("Email already exists.. try logging in");
    }
}
