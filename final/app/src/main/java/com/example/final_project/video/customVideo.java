package com.example.final_project.video;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class customVideo extends VideoView {
    public customVideo(Context context) {
        super(context);
    }
    public customVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public customVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //重写onMeasure方法，改变长宽
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(0,widthMeasureSpec);
        int height = getDefaultSize(0,heightMeasureSpec);
        setMeasuredDimension(width,height);
    }
}
