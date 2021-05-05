package com.example.final_project;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.final_project.model.Love;
import com.example.final_project.model.Video;

import java.util.Timer;
import java.util.TimerTask;



public class VideoPlayer extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "视频触控调试";
    private customVideo videoView;
    ProgressDialog pd;
    private int duration;
    private TextView tv_start;
    private TextView tv_end;
    private SeekBar seekBar;
    private ImageView imageView;
    private ImageView imageView1;
    private ImageView heart;
    private Video video;
    private Love love_layout;
    private boolean ismove =false;
    private boolean isPress = false;

    private Handler handler1 = new Handler();
    private Timer timer = new Timer();
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                tv_start.setText(calculateTime(videoView.getCurrentPosition() / 1000));
                Log.d(TAG, "进度条调试: " + videoView.getCurrentPosition() / 1000);
                seekBar.setProgress(videoView.getCurrentPosition());
            }
            super.handleMessage(msg);
        }
    };
    TimerTask task = new TimerTask(){

        public void run() {
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar()!=null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_video_player);
        Intent intent = getIntent();
        video = (Video)intent.getSerializableExtra("Video");
        //    String video_url = "https://sf3-hscdn-tos.pstatp.com/obj/developer-baas/baas/tt41nq/38d55db3a39710d0_1620049219067.mp4";
        String video_url = video.getVideoUrl();
        init_view();


        pd = new ProgressDialog(VideoPlayer.this);
        pd.setMessage("正在加载...");
        pd.setCancelable(false);
        pd.show();

        Uri videoUri = Uri.parse(video_url);
        videoView.setVideoURI(videoUri);
        videoView.requestFocus();
        videoView.getHolder().setFormat(PixelFormat.TRANSPARENT);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.setEnabled(true);
                imageView.setVisibility(View.INVISIBLE);
                duration = videoView.getDuration();
                seekBar.setMax(duration);
                Log.d(TAG, "进度条调试: " + duration);
                tv_end.setText(calculateTime(duration / 1000));
                pd.dismiss();
                videoView.start();
            }
        });
        timer.schedule(task,0,50);

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 设置视频页面不可点击，并且隐藏播放功能
                imageView.setVisibility(View.INVISIBLE);
                videoView.setEnabled(false);
                imageView1.setImageResource(R.mipmap.resume);
                imageView1.setVisibility(View.VISIBLE);
                Toast.makeText(VideoPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();
            }
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            // 发生错误重新播放
            String err = "未知错误";
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    err = "媒体服务终止";
                    break;
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    err = "未知错误";
                    break;
                default:
                    break;
            }
            Toast.makeText(VideoPlayer.this, err, Toast.LENGTH_SHORT).show();
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init_view(){
        videoView = findViewById(R.id.videoView);
        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);
        seekBar = findViewById(R.id.seekbar);
        imageView = findViewById(R.id.buttonPlayOrPause);
        imageView1 = findViewById(R.id.buttonReplay);
        love_layout = findViewById(R.id.love_layout);
        heart = findViewById(R.id.love);
        TextView userId = findViewById(R.id.userId);
        TextView username = findViewById(R.id.username);
        TextView extraText = findViewById(R.id.extraValue);
        userId.setText("@"+video.getStudentId());
        username.setText("用户 "+video.getUserName());
        extraText.setText("备注 "+video.getExtraValue());

        imageView1.setOnClickListener(this);
        videoView.setOnTouchListener((v, event) -> {
            switch ( event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    int starX = (int)event.getX();
                    int starY = (int)event.getY();
                    if(starX>(love_layout.getLeft()-30) && starY>(love_layout.getTop()+300)){
                        heart.setImageResource(R.mipmap.heart_icon2);
                        Log.d(TAG, "init_view: 超出范围");
                        return false;
                    }
                    Log.d(TAG, "init_view: 在范围内");
                    ismove = true;
                case MotionEvent.ACTION_UP:
                    if(ismove){
                        if (videoView.isPlaying()) {
                            isPress = true;
                            videoView.pause();
                            imageView.setImageResource(R.mipmap.play);
                            imageView.setVisibility(View.VISIBLE);
                        }
                        else {
                            videoView.start();
                            imageView.setImageResource(R.mipmap.pause);
                            handler1.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!isPress) {
                                        imageView.setVisibility(View.INVISIBLE);
                                    }
                                }
                            }, 500);
                            isPress = false;
                        }
                        ismove = false;
                    }
                    break;
            }
            return false;
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    tv_start.setText(calculateTime(progress/1000));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(seekBar.getProgress());
                tv_start.setText(calculateTime(videoView.getCurrentPosition()/1000));
            }
        });
    }

    private String calculateTime(int time) {
        int minute;
        int second;
        if (time >= 60) {
            minute = time / 60;
            second = time % 60;
            if (minute < 10) {
                if (second < 10) {
                    return "0" + minute + ":" + "0" + second;
                } else {
                    return "0" + minute + ":" + second;
                }
            } else {
                if (second < 10) {
                    return minute + ":" + "0" + second;
                } else {
                    return minute + ":" + second;
                }
            }
        } else {
            second = time;
            if (second >= 0 && second < 10) {
                return "00:" + "0" + second;
            } else {
                return "00:" + second;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonReplay) {
            videoView.resume();
            imageView1.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        handler1.removeCallbacks(null);
        mHandler.removeCallbacks(null);
        timer.cancel();
        imageView.setVisibility(View.INVISIBLE);
        super.onDestroy();
    }
}