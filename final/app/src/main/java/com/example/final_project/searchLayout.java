package com.example.final_project;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;


public class searchLayout extends LinearLayout  {
    private static final String TAG = "searchLayout";

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
        EditText searchEdit = findViewById(R.id.search_input);
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
                Log.i(TAG,"afterChanged" + editable.toString());
                if(mListener != null)
                    mListener.afterChanged(editable.toString());
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