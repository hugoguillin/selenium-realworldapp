package com.realworld.seleniumrealworldapp.utils.api;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.ApiService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Base64;

import static io.restassured.RestAssured.given;

@ApiService
public class AuthorApi extends ApiBase{
    @Autowired
    private ArticlesApi articlesApi;

    public void unfollowAuthor(int articleIndex) {
        String authorName = JsonPath.parse(articlesApi.getArticles(10)).read("$.articles[" + articleIndex + "].author.username");
        var authorNameEncoded = Base64.getEncoder().encodeToString(authorName.getBytes());
        given().
                header("Authorization", getToken()).
                when().
                delete("/profiles/" + authorNameEncoded + "/follow");
    }
}