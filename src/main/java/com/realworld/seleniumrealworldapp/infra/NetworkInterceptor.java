package com.realworld.seleniumrealworldapp.infra;

import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v124.network.Network;
import org.openqa.selenium.devtools.v124.network.model.Request;
import org.openqa.selenium.devtools.v124.network.model.RequestId;
import org.openqa.selenium.devtools.v124.network.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NetworkInterceptor {
    @Autowired
    private DevTools devTools;
    private final CountDownLatch latch;
    private RequestId interceptedRequestId;

    @Autowired
    public NetworkInterceptor() {
        this.latch = new CountDownLatch(1);
    }

    /**
     * Intercepts a request with a specific URL and method, so that the test can wait for it to complete.
     * Be aware that you must call {@link NetworkInterceptor#waitForResponse()} after the request is triggered.
     * @param urlRegex The regex to match the URL of the request to intercept. For example: <code>".*articles/" + variable</code>
     * @param requestMethod The method of the request to intercept
     */
    public void interceptResponse(String urlRegex, String requestMethod) {
        final Request[] req = new Request[1];
        devTools.addListener(Network.requestWillBeSent(), request -> req[0] = request.getRequest());
        devTools.addListener(Network.responseReceived(), response -> {
            Response res = response.getResponse();
            if (res.getUrl().matches(urlRegex) && req[0].getMethod().equals(requestMethod)) {
                this.interceptedRequestId = response.getRequestId();
                System.out.println("Intercepted response: " + res.getUrl() + " with status " + res.getStatus());
                markRequestAsCompleted();
            }
        });
    }

    /**
     * Waits for the intercepted request to complete and returns the response body.
     * @return The response body of the intercepted request
     */
    public String waitForResponse() {
        try {
            boolean completed = latch.await(6, TimeUnit.SECONDS);
            if (!completed) {
                throw new RuntimeException("The specific request did not complete within the timeout period");
            }
            return getResponseBody();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for request to complete", e);
        }
    }

    private String getResponseBody() {
        Network.GetResponseBodyResponse bodyResponse = devTools.send(Network.getResponseBody(interceptedRequestId));
        return bodyResponse.getBody();
    }

    private void markRequestAsCompleted() {
        latch.countDown();
    }



}
