package com.example.final_project.upload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PostApi {

    @Multipart
    @POST("video")
    Call<UploadResponse> submitMessage(@Query("student_id") String studentId,
                                       @Query("user_name") String user_name,
                                       @Query("extra_value") String extraValue,
                                       @Part MultipartBody.Part cover_image,
                                       @Part MultipartBody.Part video);
}
