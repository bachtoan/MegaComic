package com.fpoly.ph25296.assignment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServicesComic;
import com.fpoly.ph25296.assignment.adapter.AdapterComment;
import com.fpoly.ph25296.assignment.adapter.AdapterListComic_GV;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.ComicModel;
import com.fpoly.ph25296.assignment.model.CommentModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity {
    ImageButton btn_send;
    ImageView img_cover;
    TextView tv_name, tv_author, tv_username;
    RecyclerView recyclerView;

    ArrayList<CommentModel> commentModels = new ArrayList<>();
    AdapterComment adapterComment;
    EditText edt_comment;

    ComicModel comicModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        btn_send = findViewById(R.id.forum_send);
        img_cover = findViewById(R.id.dt_img);
        tv_name = findViewById(R.id.dt_name);
        tv_author = findViewById(R.id.dt_author);
        edt_comment = findViewById(R.id.edt_comment);
        tv_username = findViewById(R.id.dt_username);
        recyclerView = findViewById(R.id.rcv_comment);

        SharedPreferences sharedPreferences = getSharedPreferences("USER",MODE_PRIVATE);

        tv_username.setText(sharedPreferences.getString("name",""));

        Intent intent = getIntent();
        comicModel = (ComicModel) intent.getSerializableExtra("ObjComic");

        String imageUrl = API_URL.GET_IMAGE + comicModel.getCover();
        if(comicModel.getCover().length() > 5){
            Glide.with(this)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerInside()
                    .into(img_cover);
        }
        tv_name.setText(comicModel.getName());
        tv_author.setText(comicModel.getAuthor());

        //==============
        LoadComment();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = sharedPreferences.getString("id","");
                String username = sharedPreferences.getString("name","");
                String content = edt_comment.getText().toString();
                String comicId = comicModel.get_id();
                String image = sharedPreferences.getString("image","");

                CommentModel commentModel = new CommentModel(comicId,userId,username,content,image);
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_URL.GET_URL )
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServicesComic comicService = retrofit.create(ServicesComic.class);

                Call<CommentModel> call = comicService.postComment(commentModel);
                call.enqueue(new Callback<CommentModel>() {
                    @Override
                    public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                        if (response.isSuccessful()) {
                            CommentModel createdComic = response.body();
                            LoadComment();
                        } else {
                            Log.e("Create Comic", "Request failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<CommentModel> call, Throwable t) {
                        // Xử lý khi yêu cầu bị lỗi
                        // Log.e("Create Comic", "Request error", t);
                    }
                });
                edt_comment.setText("");

            }
        });

        img_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DetailsActivity.this,ReadActivity.class);
                intent1.putExtra("ObjComic", comicModel);
                startActivity(intent1);
            }
        });


    }


    private void LoadComment(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServicesComic servicesComic = retrofit.create(ServicesComic.class);
        Call<ComicModel> call = servicesComic.getOneComic(comicModel.get_id());
        call.enqueue(new Callback<ComicModel>() {
            @Override
            public void onResponse(Call<ComicModel> call, Response<ComicModel> response) {
                if (response.isSuccessful()) {
                    ComicModel comicModel = response.body();
                    Log.d("cmd", "onResponse: " + comicModel.getComment());
                    commentModels.clear();
                    List<Map<String, Object>> data = comicModel.getComment();

                    for (Map<String, Object> item : data) {
                        String _id = (String) item.get("_id");
                        String name = (String) item.get("name");
                        String content = (String) item.get("content");
                        String userId = (String) item.get("userId");
                        String comicId = (String) item.get("comicId");
                        String date = (String) item.get("date");
                        String image = (String) item.get("image");

                        CommentModel comment = new CommentModel(_id,comicId,userId,name,content,date,image);
                        Log.d("cmt", "onCreate: " + comment);
                        commentModels.add(comment);
                    }

                    LinearLayoutManager layoutManager = new LinearLayoutManager(DetailsActivity.this);
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    layoutManager.setSmoothScrollbarEnabled(true);
                    layoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(layoutManager);
                    adapterComment = new AdapterComment(commentModels,DetailsActivity.this);
                    recyclerView.setAdapter(adapterComment);
                } else {
                    // Xử lý lỗi khi request không thành công
                    // response.errorBody() chứa thông tin lỗi
                }
            }

            @Override
            public void onFailure(Call<ComicModel> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
            }
        });

    }

}