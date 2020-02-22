package com.example.newsapp;

import android.annotation.SuppressLint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public final class Utilities {
    private Utilities() {

    }

    public static ArrayList<NewsObject> extractNewsObjects(String jsonResponse) {
        ArrayList<NewsObject> newsItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray jsonArray = Objects.requireNonNull(jsonObject.optJSONObject(Constants.RESPONSE)).getJSONArray(Constants.RESULTS);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JSONObject = jsonArray.getJSONObject(i);
                // extracting title
                String title = JSONObject.getString(Constants.WEB_TITLE);
                // extracting section name
                String section = JSONObject.getString(Constants.SECTION_NAME);
                // sorting out the date and time
                String date = JSONObject.getString(Constants.WEB_PUBLICATION_DATE);
                int index = date.indexOf(Constants.Z);
                date = date.substring(0, index);
                date = timeConversion(date);
                // getting link to article
                String link = JSONObject.getString(Constants.WEB_URL);
                // extracting the name of the author if there is one
                String author;
                try {
                    author = JSONObject.getJSONArray(Constants.TAGS).getJSONObject(0).getString(Constants.WEB_TITLE);
                } catch (JSONException e) {
                    author = Constants.NOT_KNOWN;
                }
                // creating new NewsObject object
                NewsObject item = new NewsObject(title, section, author, date, link);
                newsItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsItems;
    }

    private static String timeConversion(String jsonTime) {
        long milliSeconds;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat guardianTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = guardianTime.parse(jsonTime);
            assert date != null;
            milliSeconds = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateConverter = new SimpleDateFormat("MMM d yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeConverter = new SimpleDateFormat("h:mm a");
        String articleDate = dateConverter.format(milliSeconds);
        String articleTime = timeConverter.format(milliSeconds);
        return articleDate + "\n" + articleTime;
    }

    public static URL createUrl(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return url;
    }

    public static String loadJSON(URL url) {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        if (url == null) {
            return null;
        }
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(Constants.GET);
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = convertInputStreamToString(inputStream);
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String output = reader.readLine();
            while (output != null) {
                builder.append(output);
                output = reader.readLine();
            }
        }
        return builder.toString();
    }


}
