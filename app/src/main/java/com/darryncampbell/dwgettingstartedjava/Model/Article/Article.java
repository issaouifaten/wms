package com.darryncampbell.dwgettingstartedjava.Model.Article;

public class Article {
    String Article;
    String Description;

    public String getArticle() {
        return Article;
    }

    public void setArticle(String article) {
        Article = article;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return "Article{" +
                "Article='" + Article + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }
}
