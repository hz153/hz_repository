package com.example.final_project.upload;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.R;

import java.io.IOException;

public class PreviewActivity extends AppCompatActivity {
    private String mp4Path;

    private SurfaceView surfaceView;
    private MediaPlayer player;

    private Boolean ButtonPressed = false;
    private boolean pauseOrplayer = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Intent intent = getIntent();
        mp4Path = intent.getStringExtra("mp4Path");
        surfaceView = findViewById(R.id.pre_surface_view);
        if(!ButtonPressed){ShowVideo();};

        Button prevButton = findViewById(R.id.prev_button);
        Button nextButton = findViewById(R.id.next_button);

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonPressed = true;
                startActivity(new Intent(PreviewActivity.this, CustomCameraActivity.class));
                finish();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonPressed = Boolean.TRUE;
                Intent intent = new Intent(PreviewActivity.this, UploadActivity.class);
                intent.putExtra("mp4Path",mp4Path);
                startActivity(intent);
                finish();
            }
        });

        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pauseOrplayer){ player.pause();}
                else{player.start();};
                pauseOrplayer = ! pauseOrplayer;
            }
        });
    }
    public static void startActivity(Context context, String mp4Path) {
        Intent intent = new Intent(context, PreviewActivity.class);
        intent.putExtra("mp4Path", mp4Path);
        context.startActivity(intent);
    }


    protected void ShowVideo() {
        player = new MediaPlayer();
        try {
            player.setDataSource(mp4Path);
            SurfaceHolder holder = surfaceView.getHolder();
            holder.setFormat(PixelFormat.TRANSPARENT);
            holder.addCallback(new PlayerCallBack());
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 循环播放
                    player.start();
                    player.setLooping(true);
                }
            });
            player.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    System.out.println(percent);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.stop();
            player.release();
        }
    }

    private class PlayerCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player.setDisplay(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}