package com.realworld.seleniumrealworldapp.utils.api;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.ApiService;
import com.realworld.seleniumrealworldapp.utils.entities.NewUserWrapper;
import com.realworld.seleniumrealworldapp.utils.entities.UserLogin;
import com.realworld.seleniumrealworldapp.utils.entities.UserLoginWrapper;

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

    public String getUser(UserLogin user) {
        var loginData = given()
            .contentType("application/json")
            .body(new UserLoginWrapper(user))
            .when()
            .post("/users/login")
            .asString();

        String token = JsonPath.parse(loginData).read("$.user.token");

        return given()
            .header("Authorization", "Token " + token)
            .when()
            .get("/user")
            .asString();
    }
}
