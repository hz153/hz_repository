package com.domker.study.androidstudy;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "调试1";
    private SurfaceView surfaceView;
    private MediaPlayer player;
    private SurfaceHolder holder;
    private SeekBar seekBar;
    private Timer timer;
    private TextView tv_start;
    private TextView tv_end;
    private RelativeLayout rl;
    private boolean isSeekbarchanged = false;
    private Button start;
    private Button pause;
    private Button resume;
    private Button change;
    private int duration;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MediaPlayer");

        setContentView(R.layout.layout_media_player);
        init_view();
        player = new MediaPlayer();
        try {
            player.setDataSource(getResources().openRawResourceFd(R.raw.big_buck_bunny));
            holder = surfaceView.getHolder();
            holder.setFormat(PixelFormat.TRANSPARENT);
            holder.addCallback(new PlayerCallBack());
            player.prepareAsync();
            tv_start.setText(calculateTime(0));
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 自动播放
                    duration = mp.getDuration();
                    seekBar.setMax(duration);
                    tv_end.setText(calculateTime(duration/1000));
                    setVideoParams(mp,getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
                    mp.start();
                    mp.setLooping(true);
                }
            });
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!isSeekbarchanged && player != null){
                        seekBar.setProgress(player.getCurrentPosition());
                        tv_start.setText(calculateTime(player.getCurrentPosition()/1000));
                    }
                }
            },0,50);
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

    private void init_view(){
        surfaceView = findViewById(R.id.surfaceView);
        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);
        seekBar = findViewById(R.id.seekbar);
        start =  findViewById(R.id.buttonPlay);
        pause = findViewById(R.id.buttonPause);
        resume = findViewById(R.id.buttonResume);
        change = findViewById(R.id.bt_change);
        rl = findViewById(R.id.rl);

        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        resume.setOnClickListener(this);
        change.setOnClickListener(this);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isSeekbarchanged = true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                isSeekbarchanged = false;
                player.seekTo(seekBar.getProgress());
                tv_start.setText(calculateTime(player.getCurrentPosition()/1000));
            }
        });
    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.buttonPlay:
                if(!player.isPlaying()){
                    player.start();
                }
                break;
            case R.id.buttonPause:
                if(player.isPlaying()){
                    player.pause();
                }
                break;
            case R.id.buttonResume:
                if(!player.isPlaying()){
                    player.seekTo(0);
                    seekBar.setProgress(0);
                    tv_start.setText(calculateTime(0));
                }
                else {
                    Toast.makeText(this,"请暂停视频",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.bt_change:
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                }
                break;
            default:
                break;
        }
    }

    private String calculateTime(int time) {
        int minute;
        int second;
        if (time >= 60) {
            minute = time / 60;
            second = time % 60;
            if (minute > 0 && minute < 10) {
                if (second >= 0 && second < 10) {
                    return "0" + minute + ":" + "0" + second;
                } else {
                    return "0" + minute + ":" + second;
                }
            } else {
                if (second >= 0 && second < 10) {
                    return minute + ":" + "0" + second;
                } else {
                    return minute + ":" + second;
                }
            }
        } else if (time < 60) {
            second = time;
            if (second >= 0 && second < 10) {
                return "00:" + "0" + second;
            } else {
                return "00:" + second;
            }
        }
        return null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            timer.cancel();
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

    private void setVideoParams(MediaPlayer mediaPlayer,boolean isLand){
        ViewGroup.LayoutParams rl_paramters = rl.getLayoutParams();
        ViewGroup.LayoutParams sv_paramters = surfaceView.getLayoutParams();
        float screen_width = getResources().getDisplayMetrics().widthPixels;
        float screen_height = getResources().getDisplayMetrics().widthPixels*9f/16f;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(isLand){
            screen_height = getResources().getDisplayMetrics().heightPixels;
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        rl_paramters.width = (int) screen_width;
        rl_paramters.height = (int) screen_height;
        int videoWidth = mediaPlayer.getVideoWidth();
        int videoHeight = mediaPlayer.getVideoHeight();
        float video_por = videoWidth / videoHeight;
        float screen_por = screen_width / screen_height;
        if (screen_por > video_por) {
            sv_paramters.height = (int) screen_height;
            sv_paramters.width = (int) (screen_height * screen_por);
        }
        else {
            sv_paramters.width = (int) screen_width;
            sv_paramters.height = (int) (screen_width / screen_por);
        }
        rl.setLayoutParams(rl_paramters);
        surfaceView.setLayoutParams(sv_paramters);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setVideoParams(player,true);
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setVideoParams(player,false);
        }
    }
}
