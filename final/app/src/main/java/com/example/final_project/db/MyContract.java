package com.example.final_project.db;

import android.provider.BaseColumns;

public final class MyContract {
    public static final String SQL_CREATE_HISTORY =
            "CREATE TABLE " + Record.TABLE_HISTORY + "("
                    + Record._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Record.ID + " TEXT, "
                    + Record.DATA_TIME + " INTEGER, "
                    + Record.STUDENT_ID + " TEXT, "
                    + Record.USER_NAME + " TEXT, "
                    + Record.EXTRA_VALUE + " TEXT, "
                    + Record.VIDEO_URL + " TEXT, "
                    + Record.IMAGE_URL + " TEXT, "
                    + Record.IMAGEW + " INTEGER, "
                    + Record.IMAGEH + " INTEGER)";

    public static final String SQL_ADD_HISTORY_COLUMN =
            "ALTER TABLE " + Record.TABLE_HISTORY + " ADD " + Record.DATA_TIME + " INTEGER";

    public static final String SQL_CREATE_FAVORITE =
            "CREATE TABLE " + Record.TABLE_FAVORITE + "("
                    + Record._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + Record.ID + " TEXT, "
                    + Record.DATA_TIME + " INTEGER, "
                    + Record.STUDENT_ID + " TEXT, "
                    + Record.USER_NAME + " TEXT, "
                    + Record.EXTRA_VALUE + " TEXT, "
                    + Record.VIDEO_URL + " TEXT, "
                    + Record.IMAGE_URL + " TEXT, "
                    + Record.IMAGEW + " INTEGER, "
                    + Record.IMAGEH + " INTEGER)";

    public static final String SQL_ADD_FAVORITE_COLUMN =
            "ALTER TABLE " + Record.TABLE_FAVORITE;

    private MyContract() {
    }

    public static class Record implements BaseColumns {
        public static final String TABLE_HISTORY ="history";
        public static final String TABLE_FAVORITE = "favorite";
        public static final String ID = "id";
        public static final String STUDENT_ID = "student_id";
        public static final String USER_NAME = "user_name";
        public static final String EXTRA_VALUE = "extra_value";
        public static final String VIDEO_URL = "video_url";
        public static final String IMAGE_URL = "image_url";
        public static final String IMAGEW = "image_w";
        public static final String IMAGEH = "image_h";
        public static final String FAVORITED = "favorited";
        public static final String DATA_TIME = "data_time";
    }
}


