package com.realworld.seleniumrealworldapp.infra;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.network.Network;
import org.openqa.selenium.devtools.v124.network.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class NetworkInterceptor {
    @Autowired
    private DevTools devTools;
    private final CountDownLatch latch;

    @Autowired
    public NetworkInterceptor() {
        this.latch = new CountDownLatch(1);
    }

    /**
     * Intercepts a request with a specific URL and method, so that the test can wait for it to complete.
     * Be aware that you must call {@link NetworkInterceptor#awaitRequestCompletion()} after the request is triggered.
     * @param urlRegex The regex to match the URL of the request to intercept. For example: <code>".*articles/" + variable</code>
     * @param requestMethod The method of the request to intercept
     */
    public void interceptRequest(String urlRegex, String requestMethod) {
        devTools.addListener(Network.requestWillBeSent(), request -> {
            Request req = request.getRequest();
            if (req.getUrl().matches(urlRegex) && req.getMethod().equals(requestMethod)) {
                System.out.println("Intercepted request: " + req.getUrl() + " with method: " + req.getMethod());
                markRequestAsCompleted();
            }
        });
    }

    public void awaitRequestCompletion() {
        try {
            boolean completed = latch.await(6, TimeUnit.SECONDS);
            if (!completed) {
                throw new RuntimeException("The specific request did not complete within the timeout period");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for request to complete", e);
        }
    }

    public void markRequestAsCompleted() {
        latch.countDown();
    }



}
