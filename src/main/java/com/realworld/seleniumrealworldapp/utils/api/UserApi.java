package com.realworld.seleniumrealworldapp.utils.api;

import com.realworld.seleniumrealworldapp.infra.annotations.ApiService;
import com.realworld.seleniumrealworldapp.utils.entities.NewUserWrapper;

import static io.restassured.RestAssured.given;

@ApiService
public class UserApi extends ApiBase {
    public void registerUser(NewUserWrapper user) {
        given()
            .contentType("application/json")
            .body(user)
            .when()
            .post("/users")
            .then()
            .statusCode(201);
    }
}
