package com.example.final_project.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.final_project.video.Video;

import java.util.ArrayList;
import java.util.List;

public class FavoriteUtil {
    public Context mContext;
    private static FavoriteUtil mFavoriteUtil;
    private FavoriteDbHelper mFavoriteDbHelper;

    private FavoriteUtil(Context context) {
        mFavoriteDbHelper = new FavoriteDbHelper(context, "FavoriteRecord.db", 2);
        this.mContext = context;
    }

    public static FavoriteUtil getInstance(Context context) {
        if (mFavoriteUtil == null) {
            mFavoriteUtil = new FavoriteUtil(context);
        } else if ((!mFavoriteUtil.mContext.getClass().equals(context.getClass()))) {
            mFavoriteUtil = new FavoriteUtil(context);
        }
        return mFavoriteUtil;
    }

    public void addFavorite(Video video) {
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        if (!isExist(video.getId())) {
            try {
                ContentValues values = new ContentValues();
                values.put(MyContract.Record.ID, video.getId());
                values.put(MyContract.Record.DATA_TIME, System.currentTimeMillis());
                values.put(MyContract.Record.STUDENT_ID, video.getStudentId());
                values.put(MyContract.Record.USER_NAME, video.getUserName());
                values.put(MyContract.Record.EXTRA_VALUE, video.getExtraValue());
                values.put(MyContract.Record.VIDEO_URL, video.getVideoUrl());
                values.put(MyContract.Record.IMAGE_URL, video.getImageUrl());
                values.put(MyContract.Record.IMAGEW, video.getImageW());
                values.put(MyContract.Record.IMAGEH, video.getImageH());
                db.insert(MyContract.Record.TABLE_FAVORITE, null, values);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //查询数据库中是否已存在某视频
    public boolean isExist(String id) {
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + MyContract.Record.TABLE_FAVORITE + " where id = ?",
                new String[]{id});
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public List<Video> queryFavoriteList() {
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        List<Video> result = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(MyContract.Record.TABLE_FAVORITE, null, null,
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

    public void deleteFavorite(String id) {
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        if (isExist(id)) {
            db.delete(MyContract.Record.TABLE_FAVORITE, "id = " + "'" + id + "'", null);
        }
    }

    public void deleteAllFavorite() {
        SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();
        db.delete(MyContract.Record.TABLE_FAVORITE,null,null);
    }
}