package com.fpoly.ph25296.assignment;

import com.fpoly.ph25296.assignment.model.ComicModel;
import com.fpoly.ph25296.assignment.model.CommentModel;
import com.fpoly.ph25296.assignment.model.Group;
import com.fpoly.ph25296.assignment.model.UsersModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServicesComic {

    @GET("groups")
    Call<ArrayList<Group>> getGroup();

    @GET("comics")
    Call<ArrayList<ComicModel>> getComic();

    @GET("comics/find")
    Call<ComicModel> getOneComic(@Query("id") String id);



    @POST("comics/add")
    Call<ComicModel> postComic(@Body ComicModel comicModel);

    @POST("comics/addComment")
    Call<CommentModel> postComment(@Body CommentModel commentModel);
    @POST("comics/deleteComment")
    Call<CommentModel> delete(@Body CommentModel commentModel);
    @Multipart
    @POST("comics/uploadCover")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image, @Part("id") String id);

    @POST("comics/delete")
    Call<Void> deleteComic(@Body ComicModel comicModel);

    @Multipart
    @POST("comics/uploadComics")
    Call<ResponseBody> uploadImages(@Part List<MultipartBody.Part> images, @Part("id") RequestBody id);


}
