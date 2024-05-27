package com.realworld.seleniumrealworldapp.pageObjects.components;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.pageObjects.BasePage;
import org.openqa.selenium.By;

@PageObject
public class TopBarPage extends BasePage {
    public String getUsername() {
        return this.getByTestId("user-pic").findElement(By.xpath("./..")).getText();
    }
}
