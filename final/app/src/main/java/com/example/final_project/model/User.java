package com.example.final_project.model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    private String id;
    @SerializedName("nick_name")
    private String nick_name;
    @SerializedName("annotation")
    private String annotation;
    @SerializedName("school")
    private String school;
    @SerializedName("phone_number")
    private String phone_number;
    @SerializedName("gender")
    private boolean gender;
    @SerializedName("birth_day")
    private String birth_day;

    public User(String ID) {
        id=ID;
        nick_name="默认昵称";
    }

    public User(String ID, String NAME) {
        id=ID;
        nick_name=NAME;
    }


    public String getId(){
        return id;
    }

    public String getNick_name() {
        return nick_name;
    }

    public String getAnnotation() {
        return annotation;
    }

    public String getBirth_day() {
        return birth_day;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getSchool() {
        return school;
    }
    public String getGender(){
        if (gender){
            return "male";
        }
        else{
            return "female";
        }
    }

}
