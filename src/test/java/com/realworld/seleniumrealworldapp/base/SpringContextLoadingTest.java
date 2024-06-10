package com.realworld.seleniumrealworldapp.base;

import com.realworld.seleniumrealworldapp.infra.webdriver.RemoteWebdriverConfig;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.IHookCallBack;
import org.testng.ITestResult;

import java.util.Objects;

@SpringBootTest
public class SpringContextLoadingTest extends AbstractTestNGSpringContextTests {

    @Override
    public void run(@NotNull IHookCallBack callBack, @NotNull ITestResult testResult) {
        String msg = "[BeforeTestExecution] Commencing to run " + testResult.getMethod().getQualifiedName()
                + "() on thread " + Thread.currentThread().getId();
        System.err.println(msg);
        WebDriver driver = Objects.requireNonNull(applicationContext).getBean(WebDriver.class);
        super.run(callBack, testResult);
        driver.quit();
    }
}
