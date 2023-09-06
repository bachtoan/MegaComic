package com.fpoly.ph25296.assignment;

import com.fpoly.ph25296.assignment.model.ComicModel;
import com.fpoly.ph25296.assignment.model.UsersModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ServiceUsers {
    @POST("users/login")
    Call<UsersModel> login(@Body UsersModel usersModel);

    @GET("users/finduser")
    Call<UsersModel> getOneUser(@Query("id") String id);



    @POST("user/adduser")
    Call<UsersModel> addUser(@Body UsersModel usersModel);
    @Multipart
    @POST("user/uploadimg")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image, @Part("id") String id);
}
