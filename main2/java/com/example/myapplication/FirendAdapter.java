package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FirendAdapter extends RecyclerView.Adapter<TextViewHolder> {
    @NonNull
    private List<String> mfriendList = new ArrayList<>();

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_list, parent, false);
        return new TextViewHolder(view);
    }
    public void onBindViewHolder(TextViewHolder holder,int position){
        holder.bind(mfriendList.get(position));
    }

    public  int getItemCount(){
        return mfriendList.size();
    }

    public void notifyItem(List<String> list){
        mfriendList.clear();
        mfriendList.addAll(list);
        notifyDataSetChanged();
    }
}
