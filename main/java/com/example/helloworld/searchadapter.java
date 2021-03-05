package com.example.helloworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class searchadapter extends RecyclerView.Adapter<TextViewHolder>  {
    @NonNull
    private List<text_item> mtextList = new ArrayList<>();

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_item, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TextViewHolder holder, int position){
        holder.bind(mtextList.get(position));
    }

    @Override
    public int getItemCount(){
        return mtextList.size();
    }

    public void notifyItems(List<text_item> mist){
        mtextList.clear();
        mtextList.addAll(mist);
        notifyDataSetChanged();
    }
}

