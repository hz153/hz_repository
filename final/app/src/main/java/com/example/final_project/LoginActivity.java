package com.example.final_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.model.LocalJSONOperator;
import com.example.final_project.model.User;

public class LoginActivity extends AppCompatActivity {
    private EditText pswd;
    private EditText ID;
    private EditText name;
    private User user;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setElevation(4f);
        actionBar.setTitle("请登录");

        setContentView(R.layout.activity_login);
        pswd = findViewById(R.id.Pswd_edit);
        ID = findViewById(R.id.ID_edit);
        name = findViewById(R.id.NickName_edit);
        Button login_btn = findViewById(R.id.LoginBtn);

        ID.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.IDBoxComplex).setBackgroundColor(0xFFFFFF);
                return false;
            }
        });
        name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.NickNameBoxComplex).setBackgroundColor(0xFFFFFF);
                return false;
            }
        });

        pswd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.PasswordBoxComplex).setBackgroundColor(0xFFFFFF);
                return false;
            }
        });

        login_btn.setOnClickListener(v -> {
            if(pswd.getText().length()!=0 && ID.getText().length()!=0){
                if(name.getText().length()==0){user=new User(ID.getText().toString());}
                else{user=new User(ID.getText().toString(),name.getText().toString());}
                LocalJSONOperator jsonOperator=new LocalJSONOperator(1,this);
                jsonOperator.setLoginInfo(user);
                startActivity(new Intent(LoginActivity.this,ScrollingActivity.class));
                this.finish();
            }
            else{
                Toast.makeText(this,"请重新输入",Toast.LENGTH_SHORT).show();
            }
        });
    }

}