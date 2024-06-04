package com.realworld.seleniumrealworldapp.infra.scope;

import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.SimpleThreadScope;

import java.util.Objects;

public class BrowserScope extends SimpleThreadScope {
    @Override
    public @NotNull Object get(@NotNull String name, @NotNull ObjectFactory<?> objectFactory) {
        Object webDriver = super.get(name, objectFactory);
        SessionId sessionId = ((RemoteWebDriver)webDriver).getSessionId();
        if (Objects.isNull(sessionId)){
            super.remove(name);
            webDriver = super.get(name, objectFactory);
        }
        return webDriver;
    }

    @Override
    public void registerDestructionCallback(@NotNull String name, @NotNull Runnable callback) {
//        super.registerDestructionCallback(name, callback);
    }
}
