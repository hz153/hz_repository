package com.example.final_project.upload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.final_project.R;
import com.example.final_project.ScrollingActivity;
import com.example.final_project.Util_;
import com.example.final_project.model.Constants;
import com.example.final_project.video.VideoPlayer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {
    private String mp4Path;
    private String CoverImagePath;
    private String extra_value;
    private TextView privacytextview;
    private ImageView edit;
    private ImageView cover;
    private Button bt_prev;
    private Button bt_upload;
    private final String TAG = "Upload";
    private static final long MAX_FILE_SIZE = 30 * 1024 * 1024;
    private EditText myTitle;
    public String privacy = "公开：所有人可见";
    private PostApi api;
    private static final int REQUEST_CODE_COVER_IMAGE = 101;
    private static final String COVER_IMAGE_TYPE = "image/*";
    private Uri coverImageUri;
    private ProgressDialog pd;

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Intent intent = getIntent();
        mp4Path = intent.getStringExtra("mp4Path");
        privacytextview = findViewById(R.id.privacy);
        edit = findViewById(R.id.edit);
        bt_prev = findViewById(R.id.prev_button);
        bt_upload = findViewById(R.id.next_button);
        myTitle = findViewById(R.id.Title);
        cover = findViewById(R.id.cover);
        CoverImagePath = getCoverImage();
        coverImageUri = Uri.fromFile(new File(CoverImagePath));
        showCoverImage(coverImageUri);
        privacytextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(privacytextview);
                privacytextview.setText(privacy);
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(privacytextview);
                privacytextview.setText(privacy);
            }
        });
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(REQUEST_CODE_COVER_IMAGE, COVER_IMAGE_TYPE, "选择图片");
            }
        });
        bt_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UploadActivity.this, PreviewActivity.class);
                intent.putExtra("mp4Path",mp4Path);
                startActivity(intent);
            }
        });
        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    submit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        myTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "beforechanged" + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG,"onTextChanged" + s);

            }

            @Override
            public void afterTextChanged(Editable s) {
                extra_value = s.toString();
            }
        });
        // 创建Retrofit实例
        // 生成api对象
        final Retrofit retrofit =  new Retrofit.Builder()
                .baseUrl("https://api-sjtu-camp-2021.bytedance.com/homework/invoke/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(PostApi.class);


    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private String getCoverImage(){
        String CoverImagePath  = null;
        Bitmap coverimage;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mp4Path);
        //下面的时间单位是微秒
        coverimage = mmr.getScaledFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST,
                200,200);

        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        CoverImagePath = mediaStorageDir.getPath() + timeStamp + ".jpg";
        File tempFile = new File(CoverImagePath);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempFile);
            coverimage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fos) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return CoverImagePath;
    }

    private void showCoverImage(Uri coverImageUri){
        cover.setImageURI(coverImageUri);
    }

    private void showPopMenu(final View view){
        final PopupMenu popupMenu = new PopupMenu(this,view);
        //menu.xml 布局
        popupMenu.getMenuInflater().inflate(R.menu.menu_upload,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.private_:
                        privacy = "私密：仅自己可见";
                        break;
                    case R.id.protected_:
                        privacy = "朋友：互相关注可见";
                        break;
                    case R.id.public_:
                        privacy = "公开：所有人可见";
                        break;
                    default:
                        break;
                }
                privacytextview.setText(privacy);
                return false;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            } });
        popupMenu.show();
    }

    private void submit() throws IOException {

        final String student_id = Constants.STUDENT_ID;
        final String user_name = Constants.USER_NAME;
        String video_name = mp4Path;
        final MultipartBody.Part video;
        byte[] coverImageData = readDataFromUri(coverImageUri);
        if (coverImageData == null || coverImageData.length == 0) {
            Toast.makeText(this, "封面不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        if ( coverImageData.length >= MAX_FILE_SIZE) {
            Toast.makeText(this, "文件过大", Toast.LENGTH_SHORT).show();
            return;
        }
        MultipartBody.Part coverImage = MultipartBody.Part.createFormData("image",CoverImagePath,
                RequestBody.create(MediaType.parse("multipart/form-data"),coverImageData));
        try{
            video = getMultipart("video",video_name);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG","视频读取失败");
            return;
        }
        pd = new ProgressDialog(UploadActivity.this);
        pd.setMessage("正在上传...");
        pd.setCancelable(false);
        pd.show();

        // 使用Retrofit同步方法请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call <UploadResponse> upload =  api.submitMessage(student_id,user_name,extra_value,coverImage,video);
                try {
                    Response<UploadResponse> response = upload.execute();
                    if (response.body().error != null){
                        Log.d(TAG,response.body().error);
                    }
                    if (response.isSuccessful() && response.body().success) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UploadActivity.this, ScrollingActivity.class));
                                UploadActivity.this.finish();
                                pd.dismiss();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(UploadActivity.this, "上传失败" , Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(UploadActivity.this, ScrollingActivity.class));
                                UploadActivity.this.finish();
                                pd.dismiss();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private MultipartBody.Part getMultipart(String name, String path) throws IOException {
        final String partKey=name;
        InputStream inputStream = new FileInputStream(path);
        byte[] bytedFile = Util_.inputStream2bytes(inputStream);
        RequestBody requestFile=RequestBody.create(MediaType.parse("multipart/from-data"),bytedFile);
        return MultipartBody.Part.createFormData(partKey,path,requestFile);
    }

    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    private byte[] readDataFromUri(Uri uri) {
        byte[] data1 = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            data1 = Util_.inputStream2bytes(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_COVER_IMAGE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverImageUri = data.getData();
                showCoverImage(coverImageUri);

                if (coverImageUri != null) {
                    Log.d(TAG, "pick cover image " + coverImageUri.toString());
                } else {
                    Log.d(TAG, "uri2File fail " + data.getData());
                }
            } else {
                Log.d(TAG, "file pick fail");
            }
        }
    }
}