package com.realworld.seleniumrealworldapp.base;

import com.realworld.seleniumrealworldapp.infra.ScreenshotUtil;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.WebDriver;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.Optional;

public class MyTestWatcher implements TestWatcher, BeforeAllCallback {

    private WebDriver driver;
    private static boolean started = false;

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        driver = SpringExtension.getApplicationContext(context).getBean(WebDriver.class);
        ScreenshotUtil screenshotUtil = SpringExtension.getApplicationContext(context).getBean(ScreenshotUtil.class);
        screenshotUtil.takeScreenshot();
        driver.quit();
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        TestWatcher.super.testDisabled(context, reason);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        driver = SpringExtension.getApplicationContext(context).getBean(WebDriver.class);
        driver.quit();
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        TestWatcher.super.testAborted(context, cause);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        if (!started) {
            FileSystemUtils.deleteRecursively(new File("allure-results"));
            FileSystemUtils.deleteRecursively(new File("allure-report"));
            started = true;
        }
    }
}

