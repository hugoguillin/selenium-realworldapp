package com.realworld.seleniumrealworldapp.utils.entities;

public class ArticleWrapper {
    private NewArticle article;

    public ArticleWrapper(NewArticle article) {
        this.article = article;
    }

    public NewArticle getArticle() {
        return article;
    }

    public void setArticle(NewArticle article) {
        this.article = article;
    }
}

