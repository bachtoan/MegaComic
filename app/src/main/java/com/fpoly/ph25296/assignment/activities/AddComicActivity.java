package com.fpoly.ph25296.assignment.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fpoly.ph25296.assignment.MainActivity;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServicesComic;
import com.fpoly.ph25296.assignment.adapter.Adapter_preview_image;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.ComicModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddComicActivity extends AppCompatActivity {
    Button button_add_comic, button_upload;
    ArrayList<Uri> imageUris = new ArrayList<>();
    LinearLayout layoutContainer;

    ActivityResultLauncher<String> pickMediaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comic);
        button_add_comic = findViewById(R.id.btn_add_page_comic);
        button_upload = findViewById(R.id.btn_upload);

        layoutContainer = findViewById(R.id.layout_container);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");


        pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {

                        Log.d("PhotoPicker", "Selected URI: " + uri.toString());
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


        button_add_comic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMediaLauncher.launch("image/*");
            }
        });

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImagesToServer(imageUris, id);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                addImageView(imageUri);
            }
        }
    }

    private void addImageView(Uri imageUri) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 8, 8, 8);
        imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.FIT_START);
        Glide.with(this)
                .load(imageUri)
                .transition(DrawableTransitionOptions.withCrossFade())
                .apply(new RequestOptions().centerCrop())
                .into(imageView);
        layoutContainer.addView(imageView);
        imageUris.add(imageUri);
    }
    private void uploadImagesToServer(ArrayList<Uri> imageUris, String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicesComic serviceApi = retrofit.create(ServicesComic.class);

        // Tạo một danh sách các MultipartBody.Part để chứa các file ảnh
        List<MultipartBody.Part> parts = new ArrayList<>();


        for (Uri uri : imageUris) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    // Tạo một File từ InputStream và sau đó tạo MultipartBody.Part từ File
                    File file = createFileFromInputStream(inputStream);
                    if (file != null) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part part = MultipartBody.Part.createFormData("images", file.getName(), requestBody);
                        parts.add(part);
                    } else {
                        Log.d("upload", "File creation failed");
                    }
                    inputStream.close();
                } else {
                    Log.d("upload", "InputStream is null");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("upload", "Exception: " + e.getMessage());
            }
        }

        RequestBody idRequestBody = RequestBody.create(MediaType.parse("text/plain"), id);

        // Gửi danh sách ảnh lên server bằng phương thức uploadImages
        Call<ResponseBody> call = serviceApi.uploadImages(parts,idRequestBody);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Xử lý kết quả thành công
                } else {
                    // Xử lý kết quả thất bại
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Xử lý khi yêu cầu bị lỗi
                Log.d("fail", "onFailure: " + t);
            }
        });
    }

    private File createFileFromInputStream(InputStream inputStream) {
        try {

            String uniqueFileName = UUID.randomUUID().toString() + ".jpg";

            File file = new File(getCacheDir(), uniqueFileName);
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
            outputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



}