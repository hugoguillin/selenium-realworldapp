package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@PageObject
public class NewArticlePage extends BasePage {
    @Value("${base.url}")
    private String baseUrl;

    public void visit() {
        driver.get(baseUrl + "/editor");
    }

    public void createArticle(Map<String, Object> articleData) {
        this.getByTestId("title").sendKeys(articleData.get("title").toString());
        this.getByTestId("description").sendKeys(articleData.get("description").toString());
        this.getByTestId("body").sendKeys(articleData.get("body").toString());
        List<String> tags = (List<String>) articleData.get("tagList");
        tags.forEach(tag -> this.getByTestId("tags").sendKeys(tag + " "));
    }

    public void editArticle(Map<String, Object> articleData) {
        wait.until(d -> this.getElementByText("Update Article").isDisplayed());
        createArticle(articleData);
    }

    public void publishArticle() {
        this.getByTestId("submit-button").click();
    }
}
