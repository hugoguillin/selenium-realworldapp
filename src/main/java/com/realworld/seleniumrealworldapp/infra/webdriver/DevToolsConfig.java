package com.realworld.seleniumrealworldapp.infra.webdriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v124.network.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.Optional;

@Configuration
public class DevToolsConfig {
    @Autowired
    protected ApplicationContext ctx;

    @Bean
    @Scope(scopeName = "webDriver")
    public DevTools initDevTools() {
         DevTools devTools = ((HasDevTools)ctx.getBean(WebDriver.class)).getDevTools();
         devTools.createSession();
         devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
         return devTools;
    }
}
