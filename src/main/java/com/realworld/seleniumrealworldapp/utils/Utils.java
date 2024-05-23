package com.realworld.seleniumrealworldapp.utils;

import com.github.javafaker.Faker;
import com.realworld.seleniumrealworldapp.utils.entities.NewArticle;
import com.realworld.seleniumrealworldapp.utils.entities.ArticleWrapper;

import java.util.*;

public class Utils {
    public static ArticleWrapper generateNewArticleData(boolean includeTags) {
        var faker = new Faker();
        var title = faker.lorem().sentence();
        var description = faker.lorem().sentence();
        var body = faker.lorem().paragraph();
        List<String> tagList = includeTags
                ? Arrays.asList(faker.name().firstName(), faker.name().firstName())
                : Collections.emptyList();

        NewArticle article = new NewArticle(title, description, body, tagList);

        return new ArticleWrapper(article);
    }
}
