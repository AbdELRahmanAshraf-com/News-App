package com.example.newsapp;

class NewsObject {
    private String mTitle;
    private String mSection;
    private String mDate;
    private String mLink;

    NewsObject(String title, String section, String date, String link) {
        this.mTitle = title;
        this.mSection = section;
        this.mDate = date;
        this.mLink = link;
    }

    String getTitle() {
        return mTitle;
    }

    String getSection() {
        return mSection;
    }

    String getDate() {
        return mDate;
    }

    String getLink() {
        return mLink;
    }
}

