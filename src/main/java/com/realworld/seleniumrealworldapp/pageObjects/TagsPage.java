package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import org.openqa.selenium.WebElement;

import java.util.List;

@PageObject
public class TagsPage extends BasePage {
    public List<String> getPopularTags() {
        return getElementsByTestId("popular-tag").stream().map(WebElement::getText).toList();
    }
}
