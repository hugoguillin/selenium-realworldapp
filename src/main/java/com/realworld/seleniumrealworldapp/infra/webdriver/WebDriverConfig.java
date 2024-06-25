package com.realworld.seleniumrealworldapp.infra.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.*;

@Configuration
@Profile("!remote")
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
    @ConditionalOnProperty(name = "browser", havingValue = "edge")
    public WebDriver edgeDriver(){
        return new EdgeDriver();
    }
}
