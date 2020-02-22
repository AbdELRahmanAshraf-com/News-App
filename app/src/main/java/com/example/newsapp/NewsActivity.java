package com.example.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsObject>> {
    private static final int NEWS_LOADER_ID = 1;
    TextView tv_noInternet;
    TextView tv_noData;
    ListView myList;
    LinearLayout linearLayout;
    StoryAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        myList = findViewById(R.id.list_view);
        linearLayout = findViewById(R.id.myLinear);
        tv_noInternet = findViewById(R.id.tv_noInternet);
        tv_noData = findViewById(R.id.tv_noData);
        if (!checkInternet()) {
            noInternet();
            tv_noData.setVisibility(View.GONE);
        } else {
            myList.setEmptyView(tv_noData);
        }
        getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    public Loader<ArrayList<NewsObject>> onCreateLoader(int id, @Nullable Bundle args) {
        String newUrl = getResources().getString(R.string.URL) + "&show-tags=contributor";
        return new MyLoader(this, newUrl);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NewsObject>> loader, ArrayList<NewsObject> data) {
        if (data != null) {
            updateUi(data);
            linearLayout.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            tv_noData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NewsObject>> loader) {
        myAdapter.clear();
    }

    private void updateUi(ArrayList<NewsObject> data) {
        myList = findViewById(R.id.list_view);
        myAdapter = new StoryAdapter(this, data);
        myList.setAdapter(myAdapter);
    }

    private boolean checkInternet() {
        ConnectivityManager manager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert manager != null;
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void noInternet() {
        linearLayout.setVisibility(View.GONE);
        tv_noInternet.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.update) {
            getSupportLoaderManager().destroyLoader(NEWS_LOADER_ID);
            if (checkInternet()) {
                tv_noInternet.setVisibility(View.GONE);
                getSupportLoaderManager().initLoader(NEWS_LOADER_ID, null, NewsActivity.this);
            } else {
                noInternet();
                tv_noData.setVisibility(View.GONE);
            }
        }
        return true;
    }
}