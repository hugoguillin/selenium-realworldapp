package com.realworld.seleniumrealworldapp.utils.api;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.infra.annotations.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static com.jayway.jsonpath.Criteria.where;

@ApiService
public class FavoritesApi extends ApiBase{
    @Value("${user.username}")
    private String username;
    @Autowired
    private ArticlesApi articlesApi;

    public String getUserFavorites(String username) {
        return given().
                param("favorited", username).
                param("limit", 10).
                when().
                get("/articles").
                asString();
    }

    public void unfavoriteArticle(int index) {
        var slug = JsonPath.parse(articlesApi.getArticles(10)).read("$.articles[" + index + "].slug");
        Filter filter = Filter.filter(where("slug").is(slug));
        List<Map<String, Object>> userFavorites = JsonPath.parse(getUserFavorites(username)).read("$.articles[?]", filter);
        if(!userFavorites.isEmpty()) {
            var finalSlug = JsonPath.parse(userFavorites).read("$[0].slug");
            given().
                    header("Authorization", getToken()).
                    when().
                    delete("/articles/" + finalSlug + "/favorite").
                    then().
                    statusCode(200);
        }
    }
}
