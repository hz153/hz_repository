package com.example.final_project.history;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.final_project.R;
import com.example.final_project.ScrollingActivity;
import com.example.final_project.video.VideoPlayer;
import com.example.final_project.db.HistoryUtil;
import com.example.final_project.video.Video;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter <HistoryAdapter.HistoryViewHolder> {
    private Context mContext;
    private List<Video> VideoList;

    public HistoryAdapter(Context context, List<Video> videoList) {
        mContext = context;
        VideoList = videoList;
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_h;
        private TextView title_h;
        private TextView user_h;
        private ImageView item_close;
        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            img_h = itemView.findViewById(R.id.img_h);
            title_h = itemView.findViewById(R.id.title_h);
            user_h = itemView.findViewById(R.id.user_h);
            item_close = itemView.findViewById(R.id.item_close);
        }
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_h,parent,false);
        HistoryViewHolder holder = new HistoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {
        Glide.with(mContext)
                .load(VideoList.get(position).getImageUrl())
                .into(holder.img_h);
        holder.title_h.setText(VideoList.get(position).getExtraValue());
        holder.user_h.setText(VideoList.get(position).getUserName());
        //点击跳转视频播放页
        //todo:这里可以考虑update历史记录数据库，实现新点击的历史记录移动到最上方
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoPlayer.class);
                intent.putExtra("Video", VideoList.get(position));
                mContext.startActivity(intent);
            }
        });
        holder.item_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HistoryUtil.getInstance(ScrollingActivity.getContext()).deleteHistory(VideoList,position);
                VideoList.clear();
                VideoList.addAll(HistoryUtil.getInstance(ScrollingActivity.getContext()).queryHistoryList());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return VideoList.size();
    }
}