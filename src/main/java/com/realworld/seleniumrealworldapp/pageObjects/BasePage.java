package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageObject
public class BasePage {
    @Autowired
    protected WebDriver driver;
    @Autowired
    protected WebDriverWait wait;

    public void clickElement(WebElement element){
        wait.until(d -> element.isDisplayed()&& element.isEnabled());
        element.click();
    }

    public void fillInputField(WebElement inputFieldElement, String textToInput) {
        wait.until(d -> inputFieldElement.isDisplayed()&&inputFieldElement.isEnabled());
        inputFieldElement.clear();
        inputFieldElement.click();
        inputFieldElement.sendKeys(textToInput);
    }

    /**
     * Finds an element by its text content
     * @param text The text content of the element
     * @return The element found
     */
    public WebElement getElementByText(String text) {
        var elementLocator = By.xpath("//*[text()=\"" + text + "\"]");
        wait.until(d -> driver.findElement(elementLocator).isDisplayed());
        return driver.findElement(elementLocator);
    }

    /**
     * Finds an element by its data-testid attribute
     * @param testId The value of the data-testid attribute
     * @return The element found
     */
    public WebElement getByTestId(String testId) {
        var elementLocator = By.cssSelector("[data-testid='" + testId + "']");
        wait.until(d -> driver.findElement(elementLocator)).isDisplayed();
        return driver.findElement(elementLocator);
    }

    /**
     * Finds elements by their data-testid attribute
     * @param testId The value of the data-testid attribute
     * @return A list of the WebElements found
     */
    public List<WebElement> getElementsByTestId(String testId) {
        var elementLocator = By.cssSelector("[data-testid='" + testId + "']");
        wait.until(d -> !driver.findElements(elementLocator).isEmpty());
        return driver.findElements(elementLocator);
    }
}
