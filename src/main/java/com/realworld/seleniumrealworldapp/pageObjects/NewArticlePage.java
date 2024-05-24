package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.entities.NewArticle;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@PageObject
public class NewArticlePage extends BasePage {
    @Value("${base.url}")
    private String baseUrl;

    public void visit() {
        driver.get(baseUrl + "/editor");
    }

    public void createArticle(NewArticle articleData) {
        this.getByTestId("title").sendKeys(articleData.getTitle());
        this.getByTestId("description").sendKeys(articleData.getDescription());
        this.getByTestId("body").sendKeys(articleData.getBody());
        List<String> tags = articleData.getTagList();
        tags.forEach(tag -> this.getByTestId("tags").sendKeys(tag + " "));

        this.getByTestId("submit-button").click();
    }

    public void editArticle(NewArticle articleData) {
        wait.until(d -> this.getElementByText("Update Article").isDisplayed());
        createArticle(articleData);
    }
}
