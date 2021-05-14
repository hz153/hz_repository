package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.model.LocalJSONOperator;
import com.example.final_project.model.User;

import java.io.File;


public class MineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(4f);
        actionBar.setTitle("设置");

        setContentView(R.layout.activity_mine);
        Button logOff = findViewById(R.id.LogOffBtn);
        TextView username = findViewById(R.id.user_name_settings);

        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        File loginFile=new File(storageDir.getAbsolutePath()+"/login.json");
        //查看登录信息
        if (loginFile.exists()) {
            LocalJSONOperator jsonOperator=new LocalJSONOperator(1,this);
            try {
                User current_user = jsonOperator.getLoginInfo();
                username.setText(current_user.getNick_name());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logOff.setOnClickListener(v -> {
            LocalJSONOperator jsonOperator=new LocalJSONOperator(1,this);
            //退出登录，把登录json文件删除，并且跳转到登陆页面
            jsonOperator.clearLoginInfo();
            startActivity(new Intent(MineActivity.this, LoginActivity.class));
            this.finish();
        });
    }
}
