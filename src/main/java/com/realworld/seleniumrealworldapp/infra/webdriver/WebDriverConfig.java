package com.realworld.seleniumrealworldapp.infra.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class WebDriverConfig {

    @Bean
    @ConditionalOnMissingBean
    @Scope(scopeName = "browserscope")
    @ConditionalOnProperty(name = "browser", havingValue = "chrome")
    public WebDriver chromeDriver(){
        return new ChromeDriver();
    }

    @Bean
    @Scope(scopeName = "browserscope")
    @ConditionalOnProperty(name = "browser", havingValue = "firefox")
    public WebDriver firefoxDriver(){
        return new FirefoxDriver();
    }

    @Bean
    @Scope(scopeName = "browserscope")
    @ConditionalOnProperty(name = "browser", havingValue = "chromeHeadless")
    public WebDriver chromeDriverHeadless(){
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        return new ChromeDriver(options);
    }

    @Bean
    @Scope(scopeName = "browserscope")
    @ConditionalOnProperty(name = "browser", havingValue = "firefoxHeadless")
    public WebDriver firefoxDriverHeadless(){
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless");
        return new FirefoxDriver(options);
    }
}
