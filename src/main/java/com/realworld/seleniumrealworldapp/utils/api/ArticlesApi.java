package com.realworld.seleniumrealworldapp.utils.api;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.ApiService;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

@ApiService
public class ArticlesApi extends ApiBase{
    @Value("${user.username}")
    private String username;

    public String getArticles(int limit) {
        return given()
                .param("limit", limit)
                .when()
                .get("/articles")
                .asString();
    }

    public void deleteArticleComments(int articleIndex) {
        String slug = JsonPath.parse(getArticles(10)).read("$.articles[" + articleIndex + "].slug");
        List<Map<String, Object>> testUserComments = JsonPath.parse(getArticleComments(slug)).read("$.comments[?(@.author.username == '" + username + "')]");
        for (var comment : testUserComments) {
            given()
                    .header("Authorization", getToken())
                    .when()
                    .delete("/articles/" + slug + "/comments/" + comment.get("id"))
                    .then()
                    .statusCode(200);
        }
    }

    public String getArticleComments(String slug) {
        return get("/articles/" + slug + "/comments")
                .asString();
    }
}
