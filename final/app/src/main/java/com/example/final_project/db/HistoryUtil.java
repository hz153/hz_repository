package com.example.final_project.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.final_project.video.Video;

import java.util.ArrayList;
import java.util.List;

public class HistoryUtil {
    public Context mContext;
    private static HistoryUtil mHistoryUtil;
    private MyDbHelper myDbHelper;

    private HistoryUtil(Context context) {
        myDbHelper = new MyDbHelper(context, "HistoryRecord.db", 1);
        this.mContext = context;
    }

    public static HistoryUtil getInstance(Context context) {
        if (mHistoryUtil == null) {
            mHistoryUtil = new HistoryUtil(context);
        } else if ((!mHistoryUtil.mContext.getClass().equals(context.getClass()))) {
            mHistoryUtil = new HistoryUtil(context);
        }
        return mHistoryUtil;
    }

    public void addHistory(List<Video> videoList, int position) {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        if (!isExist(videoList.get(position).getId())) {
            try {
                ContentValues values = new ContentValues();
                values.put(MyContract.Record.ID, videoList.get(position).getId());
                values.put(MyContract.Record.DATA_TIME, System.currentTimeMillis());
                values.put(MyContract.Record.STUDENT_ID, videoList.get(position).getStudentId());
                values.put(MyContract.Record.USER_NAME, videoList.get(position).getUserName());
                values.put(MyContract.Record.EXTRA_VALUE, videoList.get(position).getExtraValue());
                values.put(MyContract.Record.VIDEO_URL, videoList.get(position).getVideoUrl());
                values.put(MyContract.Record.IMAGE_URL, videoList.get(position).getImageUrl());
                values.put(MyContract.Record.IMAGEW, videoList.get(position).getImageW());
                values.put(MyContract.Record.IMAGEH, videoList.get(position).getImageH());
                db.insert(MyContract.Record.TABLE_HISTORY, null, values);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isExist(String id) {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MyContract.Record.TABLE_HISTORY + " where id = ?",
                new String[]{id});
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public List<Video> queryHistoryList() {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        List<Video> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(MyContract.Record.TABLE_HISTORY, null, null,
                    null, null, null, MyContract.Record.DATA_TIME + " DESC");
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(MyContract.Record.ID));
                String student_id = cursor.getString(cursor.getColumnIndex(MyContract.Record.STUDENT_ID));
                String user_name = cursor.getString(cursor.getColumnIndex(MyContract.Record.USER_NAME));
                String extra_value = cursor.getString(cursor.getColumnIndex(MyContract.Record.EXTRA_VALUE));
                String video_url = cursor.getString(cursor.getColumnIndex(MyContract.Record.VIDEO_URL));
                String image_url = cursor.getString(cursor.getColumnIndex(MyContract.Record.IMAGE_URL));
                int image_w = cursor.getInt(cursor.getColumnIndex(MyContract.Record.IMAGEW));
                int image_h = cursor.getInt(cursor.getColumnIndex(MyContract.Record.IMAGEH));

                Video video = new Video(id, student_id, user_name, extra_value, video_url, image_url);
                video.setImageW(image_w);
                video.setImageH(image_h);

                result.add(video);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void deleteHistory(List<Video> videoList, int position) {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        if (isExist(videoList.get(position).getId())){
            db.delete(MyContract.Record.TABLE_HISTORY, "id = " + "'" + videoList.get(position).getId() + "'", null);
        }
    }

    public void deleteAllHistory() {
        SQLiteDatabase db = myDbHelper.getWritableDatabase();
        db.delete(MyContract.Record.TABLE_HISTORY,null,null);
    }
}