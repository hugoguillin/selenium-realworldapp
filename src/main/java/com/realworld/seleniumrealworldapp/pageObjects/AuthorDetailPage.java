package com.realworld.seleniumrealworldapp.pageObjects;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@PageObject
public class AuthorDetailPage extends BasePage {
    @Autowired
    private ArticlesApi articlesApi;
    @Value("${base.url}")
    private String baseUrl;

    public String visit() {
        String randomIndex = String.valueOf((int) (Math.random() * 10));
        String authorName = JsonPath.read(articlesApi.getArticles(10), "$.articles["+randomIndex+"].author.username");
        driver.get(baseUrl + "/profile/" + authorName);
        wait.until(driver -> getElementByText("My Articles").getAttribute("class").contains("active"));
        return authorName;
    }
}
