package com.junjiefan.newsgateway;

import java.io.Serializable;

public class Article implements Serializable {

    private String headline;
    private String imageUrl;
    private String date;
    private String Author;
    private String text;
    private String url;




    Article(String headline, String imageUrl,String date,String author,String text,String url){

        this.headline=headline;
        this.imageUrl=imageUrl;
        this.date=date;
        this.Author=author;
        this.text=text;
        this.url=url;


    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return Author;
    }

    public String getDate() {
        return date;
    }

    public String getHeadline() {
        return headline;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }

    public String getUrl() {
        return url;
    }
}
