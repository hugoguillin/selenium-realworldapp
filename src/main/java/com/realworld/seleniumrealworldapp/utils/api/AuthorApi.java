package com.realworld.seleniumrealworldapp.utils.api;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.ApiService;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;

@ApiService
public class AuthorApi extends ApiBase{
    @Autowired
    private ArticlesApi articlesApi;

    public void unfollowAuthor(int articleIndex) {
        String authorName = JsonPath.parse(articlesApi.getArticles(10)).read("$.articles[" + articleIndex + "].author.username");
        given().
                header("Authorization", getToken()).
                when().
                delete("/profiles/" + authorName + "/follow").
                then().
                statusCode(200);
    }

    public String getAuthorArticles(String authorName) {
        return given()
                .param("author", authorName)
                .param("limit", 10)
                .header("Authorization", getToken())
                .when()
                .get("/articles")
                .asString();
    }
}
