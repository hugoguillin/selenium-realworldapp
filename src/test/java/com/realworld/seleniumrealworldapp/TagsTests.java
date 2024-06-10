package com.realworld.seleniumrealworldapp;

import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.JsonPath;
import com.realworld.seleniumrealworldapp.base.BaseTest;
import com.realworld.seleniumrealworldapp.infra.NetworkInterceptor;
import com.realworld.seleniumrealworldapp.pageObjects.TagsPage;
import com.realworld.seleniumrealworldapp.pageObjects.components.ArticlesFeedPage;
import com.realworld.seleniumrealworldapp.utils.Utils;
import com.realworld.seleniumrealworldapp.utils.api.ArticlesApi;
import com.realworld.seleniumrealworldapp.utils.api.TagsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.jayway.jsonpath.Criteria.where;
import static org.assertj.core.api.Assertions.assertThat;

public class TagsTests extends BaseTest {
    @Autowired
    private ArticlesApi articlesApi;
    @Autowired
    private TagsApi tagsApi;
    @Autowired
    private TagsPage tagsPage;
    @Autowired
    private ArticlesFeedPage articlesFeedPage;
    @Autowired
    private NetworkInterceptor networkInterceptor;

    @BeforeClass
    public void setUpSuite(ITestContext result) {
        super.setUpSuite(result);
        for (int i = 0; i < 10; i++) {
            List<String> tags = Arrays.asList("selenium", "java", "spring", "junit", "test", "automation");
            Collections.shuffle(tags);
            int maxTagsPerArticle = 3;
            var newArticle = Utils.generateNewArticleData(false);
            newArticle.getArticle().setTagList(tags.subList(0, maxTagsPerArticle));
            articlesApi.createNewArticle(newArticle);
        }
    }

    @Test(groups = {"tags"}, testName = "Should display all popular tags")
    public void displayPopularTags() {
        // Arrange
        List<String> tagsBackend = JsonPath.parse(tagsApi.getPopularTags()).read("$.tags[0:50]"); // 50 is the max number of tags displayed
        List<String> tagsFrontend = tagsPage.getPopularTags();

        // Assert
        assertThat(tagsFrontend).containsExactlyInAnyOrderElementsOf(tagsBackend);
    }

    @Test(groups = {"tags"}, testName = "Should filter articles by tag")
    public void filterArticlesByTag() {
        // Arrange
        String tag = tagsPage.getRandomTag();
        Filter filter = Filter.filter(where("tagList").contains(tag));
        List<String> articlesByTag = JsonPath.parse(articlesApi.getArticles(1000)).read("$.articles[?].title", filter);
        articlesByTag = articlesByTag.subList(0, Math.min(articlesByTag.size(), 10));

        // Act
        networkInterceptor.interceptResponse(".*/articles\\?tag=.*", "GET");
        tagsPage.filterArticlesByTag(tag);
        networkInterceptor.waitForResponse();

        // Assert
        assertThat(tagsPage.getTagTab().getText()).contains(tag);
        assertThat(articlesFeedPage.getArticleTitles()).containsExactlyInAnyOrderElementsOf(articlesByTag);
    }
}
