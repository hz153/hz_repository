package com.example.final_project.history;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.final_project.R;
import com.example.final_project.ScrollingActivity;
import com.example.final_project.db.HistoryUtil;
import com.example.final_project.video.Video;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView mRecycler;
    private HistoryAdapter historyAdapter;
    private List<Video> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("历史记录");

        historyList = HistoryUtil.getInstance(ScrollingActivity.getContext()).queryHistoryList();
        historyAdapter = new HistoryAdapter(this, historyList);
        mRecycler = findViewById(R.id.history_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(historyAdapter);
        mRecycler.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        historyAdapter.notifyDataSetChanged();

        //清空历史记录
        (findViewById(R.id.buttonClear)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryUtil.getInstance(ScrollingActivity.getContext()).deleteAllHistory();
                getHistory();
                historyAdapter.notifyDataSetChanged();
            }
        });
        //返回上一页
        (findViewById(R.id.buttonReturn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistoryActivity.this.finish();
            }
        });
    }

    private void getHistory() {
        historyList.clear();
        historyList.addAll(HistoryUtil.getInstance(ScrollingActivity.getContext()).queryHistoryList());
        historyAdapter.notifyDataSetChanged();
    }

}
