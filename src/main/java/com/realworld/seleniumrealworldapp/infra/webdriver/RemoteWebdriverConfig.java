package com.realworld.seleniumrealworldapp.infra.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;

import java.net.URL;

@Configuration
@Profile({"remote", "ci"})
public class RemoteWebdriverConfig {
    @Value("${selenium.grid.url}")
    private URL gridUrl;

    @Bean
    @Scope(scopeName = "browserscope")
    @ConditionalOnProperty(name = "browser", havingValue = "chrome")
    public WebDriver chromeDriver(){
        ChromeOptions options = new ChromeOptions();
        options.setAcceptInsecureCerts(true);
        return new RemoteWebDriver(gridUrl, options);
    }

    @Bean
    @Scope(scopeName = "browserscope")
    @ConditionalOnProperty(name = "browser", havingValue = "edge")
    public WebDriver edgeDriver(){
        return new RemoteWebDriver(gridUrl, new EdgeOptions());
    }
}
