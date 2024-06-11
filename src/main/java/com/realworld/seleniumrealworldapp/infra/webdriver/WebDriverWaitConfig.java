package com.realworld.seleniumrealworldapp.infra.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.time.Duration;
import java.util.Objects;

@Configuration
public class WebDriverWaitConfig {
    @Value("${timeout:7}")
    private int timeout;
    @Autowired
    private ApplicationContext ctx;

    @Bean
    @Scope("webDriver")
    public WebDriverWait webDriverWait(){
        WebDriver driver = ctx.getBean(WebDriver.class);
        if (Objects.isNull(((RemoteWebDriver)driver).getSessionId())) {
            driver = ctx.getBean(WebDriver.class);
        }
        return new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }
}
