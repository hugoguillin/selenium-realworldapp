package com.realworld.seleniumrealworldapp.base;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import com.realworld.seleniumrealworldapp.infra.ScreenshotUtil;
import com.realworld.seleniumrealworldapp.utils.api.ApiBase;
import io.restassured.RestAssured;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileSystemUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Objects;


public class BaseTest extends SpringContextLoadingTest {
    @Value("${base.url}")
    protected String baseUrl;
    @Value("${api.url}")
    private String apiUrl;
    @Autowired
    protected WebDriver driver;
    @Autowired
    private ApiBase apiBase;
    @Autowired
    private ScreenshotUtil screenshotUtil;
    private static boolean started = false;

    @BeforeClass
    public void setUpSuite(ITestContext result) {
        System.out.println(">>>>>>> BEFORE CLASS PARENT <<<<<<<< " + result.getName()+ "() on thread " + Thread.currentThread().getId());
        if (!started) {
            RestAssured.baseURI = apiUrl;
            storeAuthData();
            FileSystemUtils.deleteRecursively(new File("allure-results"));
            FileSystemUtils.deleteRecursively(new File("allure-report"));
            System.out.println("Report folders deleted"+ "   Thread: "+Thread.currentThread().getId());
            started = true;
        }
    }
    @BeforeMethod
    public void setUp(Method m) {
        System.out.println(">>>>>>> BEFORE METHOD PARENT <<<<<<<< " + m.getName()+ "() on thread " + Thread.currentThread().getId());
        var key = "loggedUser";
        var value = getAuthData();
        getDriver().manage().window().maximize();
        getDriver().get(baseUrl);
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        // Set auth data to local storage, so the user is logged in when the page is loaded
        js.executeScript("localStorage.setItem(arguments[0],arguments[1])",key,value);
        getDriver().navigate().refresh();
    }

    @AfterMethod
    public void tearDown(Method m, ITestResult result) {
        System.out.println(">>>>>>> AFTER METHOD <<<<<<<< " + result.getMethod().getQualifiedName());
        if (result.getStatus() == ITestResult.FAILURE){
            screenshotUtil.takeScreenshot();
            System.out.println(">>>>>>>>>>>>>> TEST FAILED <<<<<<<<<<<<<<<<");
            driver.quit();
        }
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

    protected WebDriver getDriver() {
        WebDriver driver = Objects.requireNonNull(applicationContext).getBean(WebDriver.class);
        if (Objects.isNull(((RemoteWebDriver)driver).getSessionId())) {
            driver = applicationContext.getBean(WebDriver.class);
        }
        return driver;
    }
}
