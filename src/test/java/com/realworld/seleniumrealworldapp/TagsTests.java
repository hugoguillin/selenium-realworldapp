package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.pageObjects.TagsPage;
import com.realworld.seleniumrealworldapp.utils.Utils;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import com.realworld.seleniumrealworldapp.utils.api.TagsApi;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TagsTests extends BaseTest {
    @Autowired
    private ArticlesApi articlesApi;
    @Autowired
    private TagsApi tagsApi;
    @Autowired
    private TagsPage tagsPage;

    @BeforeAll
    public void setUp() {
        super.setUpSuite();
        for (int i = 0; i < 10; i++) {
            var newArticle = Utils.generateNewArticleData(true);
            articlesApi.createNewArticle(newArticle);
        }
    }

    @BeforeEach
    public void setUpTest() {
        super.setUp();
    }

    @Test
    @Tag("tags")
    @DisplayName("Should display all popular tags")
    public void displayPopularTags() {
        // Arrange
        List<String> tagsBackend = JsonPath.parse(tagsApi.getPopularTags()).read("$.tags[0:50]"); // 50 is the max number of tags displayed
        List<String> tagsFrontend = tagsPage.getPopularTags();

        // Assert
        assertThat(tagsFrontend).containsExactlyInAnyOrderElementsOf(tagsBackend);
    }
}
