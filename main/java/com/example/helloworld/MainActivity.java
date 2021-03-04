package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SearchActivity";

    private RecyclerView mRecyclerview;
    private searchadapter mSearchAdapter = new searchadapter();
    public searchLayout msearchLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRecyclerview = findViewById(R.id.rv);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mSearchAdapter);

        final List<text_item> items = new ArrayList<>();
        for (int i = 1; i < 100; i++) {
            text_item tx =new text_item("这是第 " + i + " 行");
            items.add(tx);
        }
        mSearchAdapter.notifyItems(items);
        msearchLayout = findViewById(R.id.search);
        msearchLayout.setOnSearchTextChangedListener(new searchLayout.OnSearchTextChangedListener() {
            @Override
            public void afterChanged(String text) {
                Log.i(TAG, "afterChanged" + text);
                List<text_item> filters = new ArrayList<>();
                for(text_item item : items){
                    if(item.getText().contains(text)){
                        text_item tx1 = new text_item(item.getText());
                        filters.add(tx1);
                    }
                }
                mSearchAdapter.notifyItems(filters);
            }
        });
    }
}







