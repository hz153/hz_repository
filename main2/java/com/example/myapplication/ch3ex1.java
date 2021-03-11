package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class ch3ex1 extends AppCompatActivity {
    private SeekBar seekBar;
    private CheckBox LoopcheckBox;
    private LottieAnimationView animationView;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch3ex1);
        seekBar = findViewById(R.id.seek_bar);
        LoopcheckBox = findViewById(R.id.checkbox);
        animationView = findViewById(R.id.animation);
        mContext = ch3ex1.this;

        LoopcheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    animationView.playAnimation();
                    seekBar.setEnabled(false);
                }
                else {
                    animationView.pauseAnimation();
                    seekBar.setEnabled(true);
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, final int progress, boolean fromUser) {
                //progress是一个整数类型，然后需要转化为0-1
                Float progress1 = (float)progress/100;
                animationView.setProgress(progress1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //注意这个函数需要一个duration的参数
                Toast.makeText(mContext,"开始拖动",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(mContext,"结束拖动",Toast.LENGTH_SHORT).show();
            }
        });

    }
}