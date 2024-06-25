package com.realworld.seleniumrealworldapp.pageObjects;

import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Objects;

@PageObject
public class BasePage {
    @Autowired
    protected WebDriver driver;
    @Autowired
    protected WebDriverWait wait;
    @Autowired
    protected ApplicationContext ctx;

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
        return wait.until(d -> {
            WebElement el = getDriver().findElement(elementLocator);
            return el.isDisplayed() ? el : null;
        });
    }

    /**
     * Finds an element by its data-testid attribute
     * @param testId The value of the data-testid attribute
     * @return The element found
     */
    public WebElement getByTestId(String testId) {
        var elementLocator = By.cssSelector("[data-testid='" + testId + "']");
        return wait.until(d -> {
            WebElement el = getDriver().findElement(elementLocator);
            return el.isDisplayed() ? el : null;
        });
    }

    /**
     * Finds elements by their data-testid attribute
     * @param testId The value of the data-testid attribute
     * @return A list of the WebElements found
     */
    public List<WebElement> getElementsByTestId(String testId) {
        var elementLocator = By.cssSelector("[data-testid='" + testId + "']");
        wait.until(d -> !getDriver().findElements(elementLocator).isEmpty());
        return getDriver().findElements(elementLocator);
    }

    public WebDriver getDriver() {
        WebDriver driver = Objects.requireNonNull(ctx).getBean(WebDriver.class);
        if (Objects.isNull(((RemoteWebDriver)driver).getSessionId())) {
            driver = ctx.getBean(WebDriver.class);
        }
        return driver;
    }
}
