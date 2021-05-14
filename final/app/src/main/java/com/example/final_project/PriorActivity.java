package com.example.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class PriorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prior);
        TextView skipBtn = findViewById(R.id.skip);
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override public void run() {
                //跳转到首页
                jumpToMainActivity();
            }
        };
        final Runnable runnable2=new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                skipBtn.setText("2s（点击跳过）");
            }
        };
        final Runnable runnable1=new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                skipBtn.setText("1s（点击跳过）");
            }
        };
        handler.postDelayed(runnable2,1000);
        handler.postDelayed(runnable1,2000);
        handler.postDelayed(runnable, 3000);

        skipBtn.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
                handler.removeCallbacks(runnable);
                jumpToMainActivity();
            }
        });
    }

    private void jumpToMainActivity() {
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File loginFile=new File(storageDir.getAbsolutePath()+"/login.json");
        //查看登录信息
        if (!loginFile.exists()) {
            Log.i("File existed","Not exist");
            Toast.makeText(this,"未登录",Toast.LENGTH_SHORT).show();
            //没有登陆文件，跳转到登陆页面
            startActivity(new Intent(PriorActivity.this,LoginActivity.class));
        }
        else{
            startActivity(new Intent(PriorActivity.this,ScrollingActivity.class));
        }
    }
}