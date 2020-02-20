package com.example.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NewsActivity extends AppCompatActivity {
    TextView tv_noData;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        String url = getResources().getString(R.string.URL);
        ListView myList = findViewById(R.id.list_view);
        linearLayout = findViewById(R.id.myLinear);
        tv_noData = findViewById(R.id.tv_noData);
        MyAsyncTask myAsyncTask = new MyAsyncTask(this, myList, linearLayout, tv_noData);
        myAsyncTask.execute(url);

        // starting loader
        if (!checkInternet()) {
            noInternet();
        }
    }

    private boolean checkInternet() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    // method to inform the user if there is no internet connection
    private void noInternet() {
        linearLayout.setVisibility(View.GONE);
        tv_noData.setVisibility(View.VISIBLE);
    }
}
