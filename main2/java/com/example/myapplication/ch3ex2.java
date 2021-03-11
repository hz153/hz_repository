package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

public class ch3ex2 extends AppCompatActivity {

    private View target;
    private View Startcolor;
    private  View Endcolor;
    private Button duration_button;
    private AnimatorSet animatorSet;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ch3ex2);
        target = findViewById(R.id.target);
        Startcolor = findViewById(R.id.start_color);
        Endcolor = findViewById(R.id.end_color);
        duration_button = findViewById(R.id.duration);

        Startcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker picker = new ColorPicker(ch3ex2.this);
                picker.setColor(getBackgroundColor(Startcolor));
                picker.enableAutoClose();
                picker.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(int color) {
                        onStartColorChanged(color);
                    }
                });
                picker.show();
            }
        });

        Endcolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPicker picker = new ColorPicker(ch3ex2.this);
                picker.setColor(getBackgroundColor(Startcolor));
                picker.enableAutoClose();
                picker.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(int color) {
                        onEndColorChanged(color);
                    }
                });
                picker.show();
            }
        });

        duration_button.setText(String.valueOf(1000));
        duration_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(ch3ex2.this)
                        .inputType(InputType.TYPE_CLASS_NUMBER)
                        .input(getString(R.string.duration_hint), duration_button.getText(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                                onDurationChanged(input.toString());
                            }
                        }).show();
            }
        });
        resetAnimation();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onStartColorChanged(int color){
        Startcolor.setBackgroundColor(color);
        resetAnimation();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onEndColorChanged(int color){
        Endcolor.setBackgroundColor(color);
        resetAnimation();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void onDurationChanged(String string){
        boolean isValid = true;
        try{
            int duration = Integer.parseInt(string);
            if(duration<100 || duration>10000){
                isValid = false;
            }
        }catch (Throwable throwable){
            isValid = false;
        }
        if(isValid){
            duration_button.setText(string);
            resetAnimation();
        }
        else {
            Toast.makeText(ch3ex2.this,R.string.invalid_input,Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void resetAnimation(){
        if(animatorSet != null){
            animatorSet.cancel();
        }
        //修改颜色使用ofArgb如果是ofInt的话则是位置变换,并且需要机型支持API21
        ObjectAnimator animator1 = ObjectAnimator.ofArgb(target,"backgroundColor",
                getBackgroundColor(Startcolor),getBackgroundColor(Endcolor));
        animator1.setDuration(Integer.parseInt(duration_button.getText().toString()));
        animator1.setRepeatMode(ObjectAnimator.REVERSE);
        animator1.setRepeatCount(ObjectAnimator.INFINITE);
        ObjectAnimator Xanimator = ObjectAnimator.ofFloat(target,"scaleX",1,2);
        Xanimator.setRepeatMode(ObjectAnimator.REVERSE);
        Xanimator.setInterpolator(new LinearInterpolator());
        Xanimator.setRepeatCount(ObjectAnimator.INFINITE);
        Xanimator.setDuration(Integer.parseInt(duration_button.getText().toString()));

        ObjectAnimator Yanimator = ObjectAnimator.ofFloat(target,"scaleY",1,2);
        Yanimator.setRepeatMode(ObjectAnimator.REVERSE);
        Yanimator.setInterpolator(new LinearInterpolator());
        Yanimator.setRepeatCount(ObjectAnimator.INFINITE);
        Yanimator.setDuration(Integer.parseInt(duration_button.getText().toString()));

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator1,Xanimator,Yanimator);
        animatorSet.start();
    }

    private int getBackgroundColor(View view){
        Drawable color = view.getBackground();
        if( color instanceof ColorDrawable){
            return ((ColorDrawable) color).getColor();
        }
        else {
            return Color.WHITE;
        }
    }
}