package com.example.mobile_deadline_02;

import java.io.Serializable;

import androidx.annotation.NonNull;

public class news_item implements Serializable {
    private String title;
    private String date;
    private String description;
    private String link;

    public news_item(String title, String date, String description, String link) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
