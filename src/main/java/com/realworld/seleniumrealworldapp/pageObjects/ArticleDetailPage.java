package com.realworld.seleniumrealworldapp.pageObjects;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.PageObject;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@PageObject
public class ArticleDetailPage extends BasePage{
    @Value("${base.url}")
    private String baseUrl;
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

}
