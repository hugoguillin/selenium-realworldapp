package com.realworld.seleniumrealworldapp.utils.api;

import com.realworld.seleniumrealworldapp.infra.annotations.ApiService;

import static io.restassured.RestAssured.get;

@ApiService
public class TagsApi extends ApiBase {
    public String getPopularTags() {
        return get("/tags").
                asString();
    }
}
