package com.example.myapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TextViewHolder extends RecyclerView.ViewHolder {

    private TextView mtext;

    public TextViewHolder(View view){
        super(view);
        mtext =  view.findViewById(R.id.friend);
    }

    public void bind(String text){
        mtext.setText(text);
    }
}