package com.example.newsapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MyLoader extends AsyncTaskLoader<ArrayList<NewsObject>> {
    private URL url;

    public MyLoader(@NonNull Context context, String url) {
        super(context);
        this.url = Utilities.createUrl(url);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<NewsObject> loadInBackground() {
        if (url == null) {
            return null;
        }
        String jsonResponse = "";
        try {
            jsonResponse = Utilities.loadJSON(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Utilities.extractNewsObjects(jsonResponse);
    }
}

