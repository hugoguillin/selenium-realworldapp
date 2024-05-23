package com.realworld.seleniumrealworldapp.pageObjects;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

@PageObject
public class ArticleDetailPage extends BasePage{
    @Value("${base.url}")
    private String baseUrl;
    @Value("${user.username}")
    private String username;
    @Autowired
    private ArticlesApi articlesApi;

    public void visit(int articleIndex) {
        var slug = JsonPath.parse(articlesApi.getArticles(10)).read("$.articles[" + articleIndex + "].slug");
        driver.get(baseUrl + "/article/" + slug);
    }

    public void goToArticle(String slug) {
        driver.get(baseUrl + "/article/" + slug);
    }

    public void sendComment(String message) {
        this.getByTestId("comment-textarea").sendKeys(message);
        this.getByTestId("post-comment").click();
    }

    public void assertCommentIsVisible(String message) {
        wait.until(driver -> this.getElementByText(message).isDisplayed());
    }

    public void assertCommentHasTestUserUsername(String message) {
        WebElement el = getElementsByTestId("comment-card").stream()
                .filter(card -> card.getText().contains(message))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Comment card not found"));
        assertThat(el.findElement(By.cssSelector("[data-testid='author-username']")).getText()).contains(username);
    }

    public void deleteComment(String message) {
        var commentCard = getElementsByTestId("comment-card").stream()
                .filter(card -> card.getText().contains(message))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Comment card not found"));
        commentCard.findElement(By.cssSelector("[data-testid='delete-comment']")).click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public void assertCommentIsNotVisible(String message) {
        wait.until(d -> {
            try {
                return !this.getElementByText(message).isDisplayed();
            } catch (Exception e) {
                return true;
            }
        });
    }

    public void deleteArticle() {
        this.getByTestId("delete-article").click();
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public void assertAppNavigatesToHomePageAfterArticleDeletion() {
        wait.until(ExpectedConditions.urlToBe(baseUrl + "/"));
    }

    public void assertThatNavigatingToDeletedArticleReturns404(String slug) {
        driver.get(baseUrl + "/article/" + slug);
        assertThat(getElementByText("404 Not Found").isDisplayed()).isTrue();
        assertThat(driver.findElement(By.tagName("a")).getAttribute("href")).isEqualTo(baseUrl + "/");
    }

    public void assertThatArticleDisplaysExpectedData(Map<String, Object> articleData) {
        List<String> expectedTags = (List<String>) articleData.get("tagList");
        List<String> actualTags = getElementsByTestId("article-tag").stream()
                .map(WebElement::getText)
                .toList();
        assertThat(driver.findElement(By.tagName("p")).getText()).isEqualTo(articleData.get("body").toString());
        assertThat(expectedTags).containsExactlyInAnyOrderElementsOf(actualTags);
    }
}
