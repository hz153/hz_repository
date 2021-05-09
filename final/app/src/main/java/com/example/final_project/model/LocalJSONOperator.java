package com.example.final_project.model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

public class LocalJSONOperator {
    private File target;
    public int LOGIN = 1;
    private Context context;

    public LocalJSONOperator(File file, Context c){
        target = file;
        context = c;
    }

    public LocalJSONOperator(int mode, Context c){
        context = c;
        if (mode == 1) {
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            target = new File(storageDir.getAbsolutePath() + "/login.json");
        }
    }

    public User getLoginInfo(){
        String jsonS=getJson(target);
        return new Gson().fromJson(jsonS,User.class);
    }

    public void setLoginInfo(User user){
        Log.i("File exists",String.valueOf(target.exists()));
        try (Writer writer = new FileWriter(target)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(user, writer);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearLoginInfo(){
        if (target.exists() && target.isFile()){
            target.delete();
        }
    }

    //设置缓冲读，并将缓冲的字符串返回
    public static String getJson(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            //一行一行的读
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
