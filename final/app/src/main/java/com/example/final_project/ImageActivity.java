package com.example.final_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.model.Video;
import com.example.final_project.model.VideoListResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ImageActivity extends AppCompatActivity {
    private final String TAG = "网络调试";
    private Button button1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        button1 = findViewById(R.id.btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData("519020910274");
            }
        });

    }


    private void getData(String studentId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Video> video_list = baseGetVideoFromRemote(studentId);
                Log.d(TAG, "run: "+video_list.get(0).getVideoUrl());
                activity_jump(video_list.get(0));
            }
        }).start();
    }

    private void activity_jump(Video video){
        Intent intent = new Intent(ImageActivity.this, VideoPlayer.class);
        intent.putExtra("Video", video);
        startActivity(intent);
    }


    public List<Video> baseGetVideoFromRemote(String studentId){
        String urlStr =
                String.format("https://api-sjtu-camp-2021.bytedance.com/homework/invoke/video?student_id=%s",studentId);
        VideoListResponse result = null;
        try{
            URL url  = new URL(urlStr);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(6000);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("token", Constants.token);
            if (connection.getResponseCode() == 200){
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
                result = new Gson().fromJson(reader, new TypeToken<VideoListResponse>(){
                }.getType());

                reader.close();
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.feeds;
    }
}
