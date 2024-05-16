package com.realworld.seleniumrealworldapp.utils.api;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.ApiService;
import org.springframework.beans.factory.annotation.Value;
import static io.restassured.RestAssured.*;

@ApiService
public class ApiBase {
    @Value("${api.url}")
    private String apiUrl;
    @Value("${user.email}")
    private String userEmail;
    @Value("${user.password}")
    private String userPassword;

    public String getLoggedInUserData() {
        return given().
                contentType("application/json").
                body("{\"user\":{\"email\":\""+ userEmail +"\",\"password\":\""+ userPassword +"\"}}").
                when().
                post(apiUrl + "/users/login").
                asString();
    }

    public  String getToken() {
        var response =  getLoggedInUserData();
        String token = JsonPath.parse(response).read("$.user.token");
        return "Token " + token;
    }
}
