package com.realworld.seleniumrealworldapp.utils.api;

import com.realworld.seleniumrealworldapp.infra.annotations.ApiService;

import static io.restassured.RestAssured.given;

@ApiService
public class ArticlesApi {
    public String getArticles(int limit) {
        return given()
                .param("limit", limit)
                .when()
                .get("/articles")
                .asString();
    }


}
