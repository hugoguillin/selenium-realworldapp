package com.realworld.seleniumrealworldapp.pageObjects;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public void giveLikeToAnArticle(int articleIndex) {
        wait.until(driver -> this.getElementsByTestId("fav-button").get(articleIndex).isDisplayed());
        this.getElementsByTestId("fav-button").get(articleIndex).click();
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

}
