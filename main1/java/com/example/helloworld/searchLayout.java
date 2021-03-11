package com.example.helloworld;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class searchLayout extends LinearLayout  {
    private static final String TAG = "searchLayout";

    private EditText searchEdit;
    private ImageView searchimage;
    private Button mCancel;

    private OnSearchTextChangedListener mListener;

    public searchLayout(Context context){
        super(context);
        initView();
    }

    public searchLayout(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        initView();
    }

    public searchLayout(Context context,AttributeSet attributeSet,int defstleAtter){
        super(context, attributeSet, defstleAtter);
        initView();
    }

    private void initView(){
        LayoutInflater.from(getContext()).inflate(R.layout.search_item,this);
        searchimage = findViewById(R.id.search_image);
        searchEdit = findViewById(R.id.search_input);
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "beforechanged" + charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG,"onTextChanged" + charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG,"afterChanged" + editable);
                if(mListener != null)
                    mListener.afterChanged(editable.toString());
            }
        });

        mCancel = findViewById(R.id.search_btn_back);
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
    }

    public void setOnSearchTextChangedListener(OnSearchTextChangedListener listener){
        mListener = listener;
    }

    public interface OnSearchTextChangedListener{
        void afterChanged(String text);
    }

}
