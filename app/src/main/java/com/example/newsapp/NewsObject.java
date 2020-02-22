package com.example.newsapp;

class NewsObject {
    private String mTitle;
    private String mSection;
    private String mDate;
    private String mLink;
    private String mAuthor;


    NewsObject(String title, String section, String mAuthor, String date, String link) {
        this.mTitle = title;
        this.mSection = section;
        this.mDate = date;
        this.mLink = link;
        this.mAuthor = mAuthor;
    }

    public String getmAuthor() {
        return mAuthor;
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

