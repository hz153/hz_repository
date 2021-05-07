package com.example.final_project;

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
import com.example.final_project.model.Video;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter <MyAdapter.MyViewHolder> {
    private List<Video> VideoList;
    private Context mContext;

    public void setData(List<Video> videoList, Context context){
        mContext = context;
        VideoList = videoList;
        notifyDataSetChanged();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView grid_img;
        private TextView grid_title;
        private TextView grid_user;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            grid_img = itemView.findViewById(R.id.grid_img);
            grid_title = itemView.findViewById(R.id.grid_title);
            grid_user = itemView.findViewById(R.id.grid_user);
        }
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        //加载图片和文字
        Glide.with(mContext)
                .load(VideoList.get(position).getImageUrl())
                .into(holder.grid_img);
        holder.grid_title.setText(VideoList.get(position).getExtraValue());
        holder.grid_user.setText(VideoList.get(position).getUserName());
        //todo:点击item跳转 并在其他页面补充相同事件
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, VideoPlayer.class);
                intent.putExtra("Video", VideoList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return VideoList==null?0:VideoList.size();
    }
}
