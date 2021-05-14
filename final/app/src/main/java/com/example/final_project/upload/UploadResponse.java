package com.example.final_project.upload;

import android.os.Message;

import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("result")
    public Message message;
    @SerializedName("success")
    public boolean success;
    @SerializedName("error")
    public String error;
}
