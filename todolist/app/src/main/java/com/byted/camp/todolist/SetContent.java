package com.byted.camp.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byted.camp.todolist.db.TodoContract.TodoNote;
import com.byted.camp.todolist.db.TodoDbHelper;

public class SetContent extends AppCompatActivity {
    private final static String TAG = "调试1";

    private EditText changeText;
    private long id;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_content);
        setTitle(R.string.change_note);

        TodoDbHelper dbHelper = new TodoDbHelper(this);
        database = dbHelper.getWritableDatabase();
        Intent intent = getIntent();
        id = intent.getLongExtra("id",0);
        Log.d(TAG, "onCreate: "+id);

        changeText = findViewById(R.id.change_text);
        changeText.setFocusable(true);
        changeText.requestFocus();
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.showSoftInput(changeText, 0);
        }
        Button changeBtn = findViewById(R.id.btn_change);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence content = changeText.getText();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(SetContent.this,
                            "No content to add", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean succeed = change_content(content.toString().trim());
                if (succeed) {
                    Toast.makeText(SetContent.this,
                            "Note changed", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(SetContent.this,
                            "Error", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });

    }

    private boolean change_content(String content){
        if (database == null || TextUtils.isEmpty(content)) {
            return false;
        }
        ContentValues values = new ContentValues();
        values.put(TodoNote.COLUMN_CONTENT, content);

        int rows = database.update(TodoNote.TABLE_NAME, values,
                TodoNote._ID + "=?",
                new String[]{String.valueOf(id)});
        if (rows > 0) {
            return true;
        }
        else {
            return false;
        }
    }
}