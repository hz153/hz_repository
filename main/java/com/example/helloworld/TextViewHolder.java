package com.example.helloworld;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mtext;

    public TextViewHolder(View view){
        super(view);
        mtext = (TextView) view.findViewById(R.id.text);
        view.setOnClickListener(this);
    }

    public void bind(text_item text){
        mtext.setText(text.getText());
    }

    @Override
    public void onClick(View view){
        Intent intent = new Intent(view.getContext(),MainActivity3.class);
        intent.putExtra("extra",mtext.getText());
        view.getContext().startActivity(intent);
    }


}
