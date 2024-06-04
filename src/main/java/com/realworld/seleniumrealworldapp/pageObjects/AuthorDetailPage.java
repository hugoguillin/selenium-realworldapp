package com.realworld.seleniumrealworldapp.pageObjects;

import com.google.common.net.MediaType;
import com.google.common.util.concurrent.Uninterruptibles;
import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import org.openqa.selenium.devtools.NetworkInterceptor;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.http.Contents;
import org.openqa.selenium.remote.http.HttpResponse;
import org.openqa.selenium.remote.http.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;


@PageObject
public class AuthorDetailPage extends BasePage {
    @Autowired
    private ArticlesApi articlesApi;
    @Value("${base.url}")
    private String baseUrl;

    /**
     * Visits an author's profile page
     * @return authorName
     */
    public String visit(int articleIndex) {
        String authorName = JsonPath.read(articlesApi.getArticles(10), "$.articles["+articleIndex+"].author.username");
        driver.get(baseUrl + "/profile/" + authorName);
        wait.until(driver -> getElementByText("My Articles").getAttribute("class").contains("active"));
        return authorName;
    }

    /**
     * Mocks the API response to display the favorited articles
     */
    public void goToFavoritedArticles(Object file) {
        driver = new Augmenter().augment(driver);
        try (NetworkInterceptor ignored = new NetworkInterceptor(
                driver, Route.matching(req -> req.getUri().matches(".*articles\\?favorited=.*"))
                .to(() -> req ->
                        new HttpResponse()
                                .setStatus(200)
                                .addHeader("Content-Type", MediaType.JSON_UTF_8.toString())
                                .setContent(Contents.asJson(file))))) {
            getElementByText("Favorited Articles").click();
            Uninterruptibles.sleepUninterruptibly(200, TimeUnit.MILLISECONDS);
        }
    }
}
