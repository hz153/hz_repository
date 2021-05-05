package com.example.final_project.model;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.final_project.R;

import java.util.Random;

public class Love extends RelativeLayout {
    private final String TAG ="爱心调试";
    private Context mContext;
    float[] num = {-30, -15, 0, 15, 30};//随机心形图片角度

    public Love(Context context) {
        super(context);
        initView(context);
    }

    public Love(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public Love(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if((int)event.getY()>450){
            final ImageView imageView = new ImageView(mContext);
            LayoutParams params = new LayoutParams(150, 150);
            params.leftMargin = (int) event.getX() - 70;
            params.topMargin = (int) event.getY() - 150;
            imageView.setImageResource(R.mipmap.heart_icon2);
            imageView.setLayoutParams(params);
            addView(imageView);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.play(scale(imageView, "scaleX", 1.5f, 0.9f, 100, 0))
                    .with(scale(imageView, "scaleY", 1.5f, 0.9f, 100, 0))
                    .with(rotation(imageView, 0, 0, num[new Random().nextInt(5)]))
                    .with(alpha(imageView, 0, 1, 100, 0))
                    .with(scale(imageView, "scaleX", 0.9f, 1, 50, 150))
                    .with(scale(imageView, "scaleY", 0.9f, 1, 50, 150))
                    .with(alpha(imageView, 1, 0, 300, 400))
                    .with(scale(imageView, "scaleX", 1, 3f, 700, 400))
                    .with(scale(imageView, "scaleY", 1, 3f, 700, 400))
                    .with(translationY(imageView, 0, -600, 800, 400));

            animatorSet.start();
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    removeViewInLayout(imageView);
                }
            });
        }
        return super.onTouchEvent(event);
    }

    public static ObjectAnimator scale(View view, String propertyName, float from, float to, long duration_time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , propertyName
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(duration_time);
        return translation;
    }

    public static ObjectAnimator alpha(View view, float from, float to, long duration_time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "alpha"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(duration_time);
        return translation;
    }

    public static ObjectAnimator rotation(View view, long duration_time, long delayTime, float values) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", values);
        rotation.setDuration(duration_time);
        rotation.setStartDelay(delayTime);
        rotation.setInterpolator(new LinearInterpolator());
        return rotation;
    }

    public static ObjectAnimator translationY(View view, float from, float to, long time, long delayTime) {
        ObjectAnimator translation = ObjectAnimator.ofFloat(view
                , "translationY"
                , from, to);
        translation.setInterpolator(new LinearInterpolator());
        translation.setStartDelay(delayTime);
        translation.setDuration(time);
        return translation;
    }
}
