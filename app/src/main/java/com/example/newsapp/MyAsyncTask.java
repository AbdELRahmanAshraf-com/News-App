package com.example.newsapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class MyAsyncTask extends AsyncTask<String, String, String> {
    @SuppressLint("StaticFieldLeak")
    private Context context;
    @SuppressLint("StaticFieldLeak")
    private ListView listView;
    @SuppressLint("StaticFieldLeak")
    private LinearLayout linearLayout;
    @SuppressLint("StaticFieldLeak")
    private TextView textView;
    private Boolean isConnected = true;

    MyAsyncTask(Context context, ListView listView, LinearLayout linearLayout, TextView textView) {
        this.context = context;
        this.listView = listView;
        this.linearLayout = linearLayout;
        this.textView = textView;
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

    @SuppressLint("WrongThread")
    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(context.getResources().getString(R.string.URL));
            String jsonResponse = "";
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = convertInputStreamToString(inputStream);
                publishProgress(jsonResponse);
            } else {
                isConnected = false;
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
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        linearLayout.setVisibility(View.GONE);
        if (!isConnected) {
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        ArrayList<NewsObject> newsItems = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(values[0]);
            JSONArray jsonArray = Objects.requireNonNull(jsonObject.optJSONObject("response")).getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JSONObject = jsonArray.getJSONObject(i);
                // extracting title
                String title = JSONObject.getString("webTitle");
                // extracting section name
                String section = JSONObject.getString("sectionName");
                // sorting out the date and time
                String date = JSONObject.getString("webPublicationDate");
                int index = date.indexOf("Z");
                date = date.substring(0, index);
                date = timeConversion(date);
                // getting link to article
                String link = JSONObject.getString("webUrl");
                // creating new NewsObject object
                NewsObject item = new NewsObject(title, section, date, link);
                newsItems.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (newsItems.isEmpty()) {
            textView.setVisibility(View.VISIBLE);
        } else {
            StoryAdapter myAdapter = new StoryAdapter(context, newsItems);
            listView.setAdapter(myAdapter);
            textView.setVisibility(View.GONE);
        }
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {
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
