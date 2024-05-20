package com.realworld.seleniumrealworldapp.pageObjects.components;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.pageObjects.BasePage;
import org.openqa.selenium.WebElement;

import java.util.List;

@PageObject
public class ArticlesFeedPage extends BasePage {
    public List<String> getArticleTitles() {
        return getElementsByTestId("article-title").stream().map(WebElement::getText).toList();
    }
}
