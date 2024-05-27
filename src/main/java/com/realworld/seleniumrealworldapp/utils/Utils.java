package com.realworld.seleniumrealworldapp.utils;

import com.github.javafaker.Faker;
import com.realworld.seleniumrealworldapp.utils.entities.*;

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

    public static NewUserWrapper generateNewUserData() {
        var faker = new Faker();
        var username = faker.name().username();
        var email = faker.internet().emailAddress();
        var password = faker.internet().password();

        var newUser = new NewUser(username, email, password);
        return new NewUserWrapper(newUser);
    }

    public static UserSettings generateUserSettingsData() {
        var faker = new Faker();
        var image = faker.internet().avatar();
        var username = faker.name().username();
        var bio = faker.lorem().sentence();
        var email = faker.internet().emailAddress();
        var password = faker.internet().password();

        return new UserSettings(image, username, bio, email, password);
    }
}
