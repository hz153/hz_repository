package com.example.final_project.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VideoListResponse {
    @SerializedName("feeds")
    public List<Video> feeds;
    @SerializedName("success")
    public boolean success;
}
