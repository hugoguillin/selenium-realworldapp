package com.realworld.seleniumrealworldapp.base;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.realworld.seleniumrealworldapp.utils.api.ApiBase;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@SpringBootTest
@ExtendWith(MyTestWatcher.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ComponentScan("com.realworld.seleniumrealworldapp")
public class BaseTest {
    @Value("${browser}")
    private String browser;
    @Value("${base.url}")
    private String baseUrl;
    @Value("${api.url}")
    private String apiUrl;
    @Autowired
    private WebDriver driver;
    @Autowired
    protected ApplicationContext ctx;
    @Autowired
    private ApiBase apiBase;

    @BeforeAll
    public void setUpSuite() {
        RestAssured.baseURI = apiUrl;
        storeAuthData();
    }
    @BeforeEach
    public void setUp() {
        var key = "loggedUser";
        var value = getAuthData();
        initDriver();
        driver.get(baseUrl);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Set auth data to local storage, so the user is logged in when the page is loaded
        js.executeScript("localStorage.setItem(arguments[0],arguments[1])",key,value);
        driver.navigate().refresh();
    }

    private void initDriver(){
        switch (browser) {
            case "firefox" -> driver = ctx.getBean("firefoxDriver", WebDriver.class);
            case "firefoxHeadless" -> driver = ctx.getBean("firefoxDriverHeadless", WebDriver.class);
            case "chrome" -> driver = ctx.getBean("chromeDriver", WebDriver.class);
            case "chromeHeadless" -> driver = ctx.getBean("chromeDriverHeadless", WebDriver.class);
        }
        Dimension dimension = new Dimension(1920, 1080);
        driver.manage().window().setSize(dimension);
    }

    /**
     * Get user auth data from the API and store it in a file with the same format as the local storage
     */
    private void storeAuthData() {
        File authFile = new File("auth.json");
        var res = apiBase.getLoggedInUserData();
        ReadContext ctx = JsonPath.parse(res);
        String token = ctx.read("$.user.token");
        String email = ctx.read("$.user.email");
        String username = ctx.read("$.user.username");
        var value = "{\"headers\":{\"Authorization\":\"Token " + token + "\"},\"isAuth\":true,\"loggedUser\":{\"email\":\"" + email + "\",\"username\":\""+ username +"\",\"bio\":null,\"image\":null,\"token\":\""+ token +"\"}}";
        try (FileWriter fileWriter = new FileWriter(authFile)) {
            fileWriter.write(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getAuthData() {
        var data = "";
        try (FileReader fileReader = new FileReader("auth.json")) {
            StringBuilder sb = new StringBuilder();
            int ch;
            while ((ch = fileReader.read()) != -1) {
                sb.append((char) ch);
            }
            data = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}
