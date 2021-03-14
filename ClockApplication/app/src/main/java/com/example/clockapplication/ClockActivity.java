package com.example.clockapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class ClockActivity extends AppCompatActivity {

    private View mRootView;
    private Clock mClockView;
    private ClockHandlerThread clockHandlerThread = new ClockHandlerThread("abc");
    class ClockHandlerThread extends HandlerThread implements Handler.Callback {

        public static final int MSG_QUERY_STOCK = 100;

        private Handler mHandler;

        public ClockHandlerThread(String name) {
            super(name);
        }

        public ClockHandlerThread(String name, int priority){
            super(name);
        }

        @Override
        protected void onLooperPrepared() {
            mHandler = new Handler(getLooper(), this);
            mHandler.sendEmptyMessage(MSG_QUERY_STOCK);
        }

        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == MSG_QUERY_STOCK) {
                mClockView.invalidate();
                Log.e("StockHandlerThread", "handleMessage :" + msg.what);
                mHandler.sendEmptyMessageDelayed(MSG_QUERY_STOCK, 1000);
            }
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        mRootView = findViewById(R.id.root);
        mClockView = findViewById(R.id.clock);
        clockHandlerThread.start();
    }
}