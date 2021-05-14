package com.example.final_project.favorite;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.ScrollingActivity;
import com.example.final_project.db.FavoriteUtil;
import com.example.final_project.db.HistoryUtil;
import com.example.final_project.video.Video;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private FavoriteAdapter favoriteAdapter;
    private List<Video> favoriteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        setTitle("收藏夹");

        favoriteList = FavoriteUtil.getInstance(ScrollingActivity.getContext()).queryFavoriteList();
        favoriteAdapter = new FavoriteAdapter(this, favoriteList);
        mRecycler = findViewById(R.id.favorite_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(favoriteAdapter);
        mRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        favoriteAdapter.notifyDataSetChanged();

        //清空收藏夹
        (findViewById(R.id.buttonClear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteUtil.getInstance(ScrollingActivity.getContext()).deleteAllFavorite();
                getFavorite();
                favoriteAdapter.notifyDataSetChanged();
            }
        });
        //返回上一页
        (findViewById(R.id.buttonReturn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteActivity.this.finish();
            }
        });
    }

    private void getFavorite() {
        favoriteList.clear();
        favoriteList.addAll(FavoriteUtil.getInstance(ScrollingActivity.getContext()).queryFavoriteList());
        favoriteAdapter.notifyDataSetChanged();
    }
}
