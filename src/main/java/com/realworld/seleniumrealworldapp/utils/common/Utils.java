package com.realworld.seleniumrealworldapp.utils.common;

import com.github.javafaker.Faker;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Map<String, Object> generateNewArticleData(boolean includeTags) {
        var faker = new Faker();
        var title = faker.lorem().sentence();
        var description = faker.lorem().sentence();
        var body = faker.lorem().paragraph();
        var tagList = includeTags ? Arrays.asList(faker.name().firstName(), faker.name().firstName()) : Collections.emptyList();

        // Create the map for the "article" object
        Map<String, Object> articleMap = new HashMap<>();
        articleMap.put("title", title);
        articleMap.put("description", description);
        articleMap.put("body", body);
        articleMap.put("tagList", tagList);

        // Create the main map
        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put("article", articleMap);

        return mainMap;
    }
}
