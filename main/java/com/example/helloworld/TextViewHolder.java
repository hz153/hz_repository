package com.example.helloworld;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TextViewHolder extends RecyclerView.ViewHolder {

    private TextView mtext;

    public TextViewHolder(View view){
        super(view);
        mtext = (TextView) view.findViewById(R.id.text);
    }

    public void bind(text_item text){
        mtext.setText(text.getText());
    }
}
