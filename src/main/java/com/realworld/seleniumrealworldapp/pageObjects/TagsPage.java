package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import org.openqa.selenium.WebElement;

import java.util.List;

@PageObject
public class TagsPage extends BasePage {

    public List<String> getPopularTags() {
        return getTags().stream().map(WebElement::getText).toList();
    }

    public String getRandomTag() {
        int randomIndex = (int) (Math.random() * 5);
        return getTags().get(randomIndex).getText();
    }

    public void filterArticlesByTag(String tag) {
        getTags().stream()
                .filter(element -> element.getText().equals(tag))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tag not found"))
                .click();
    }

    public WebElement getTagTab() {
        return getByTestId("tag-feed");
    }

    private List<WebElement> getTags() {
        return getElementsByTestId("popular-tag");
    }
}
