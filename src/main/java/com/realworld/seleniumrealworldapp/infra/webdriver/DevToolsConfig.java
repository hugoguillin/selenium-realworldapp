package com.realworld.seleniumrealworldapp.infra.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v125.network.Network;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Objects;
import java.util.Optional;

@Configuration
public class DevToolsConfig {
    @Autowired
    protected ApplicationContext ctx;

    @Bean
    @Scope(scopeName = "webDriver")
    public DevTools initDevTools() {
        WebDriver driver = ctx.getBean(WebDriver.class);
        if (Objects.isNull(((RemoteWebDriver)driver).getSessionId())) {
            driver = ctx.getBean(WebDriver.class);
        }
        driver = new Augmenter().augment(driver); // Augment the driver to allow for DevTools when using RemoteWebDriver
        DevTools devTools = ((HasDevTools)driver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
        return devTools;
    }
}
