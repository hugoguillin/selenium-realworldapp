package com.realworld.seleniumrealworldapp.infra.scope;

import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.SimpleThreadScope;

import java.util.Objects;

public class BrowserScope extends SimpleThreadScope {
    @SuppressWarnings("NullableProblems")
    @Override
    public Object get(String name, ObjectFactory<?> objectFactory) {
        Object webDriver = super.get(name, objectFactory);
        SessionId sessionId = ((RemoteWebDriver)webDriver).getSessionId();
        if (Objects.isNull(sessionId)){
            super.remove(name);
            webDriver = super.get(name, objectFactory);
        }
        return webDriver;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void registerDestructionCallback(String name, Runnable callback) {
//        super.registerDestructionCallback(name, callback);
    }
}
