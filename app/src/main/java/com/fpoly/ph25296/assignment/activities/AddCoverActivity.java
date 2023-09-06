package com.fpoly.ph25296.assignment.activities;

import androidx.activity.result.ActivityResultLauncher;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServicesComic;
import com.fpoly.ph25296.assignment.model.API_URL;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

public class AddCoverActivity extends AppCompatActivity {

    ActivityResultLauncher<String> pickMediaLauncher;
    ImageView imageView;
    Button btn_save,btn_pick;

    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cover);
        imageView = findViewById(R.id.img_cover);
        btn_pick = findViewById(R.id.btn_pick_cover);
        btn_save = findViewById(R.id.btn_save_cover);


        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        pickMediaLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        Glide.with(this)
                                .load(uri)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(new RequestOptions().centerCrop())
                                .into(imageView);
                        Log.d("PhotoPicker", "Selected URI: " + uri.toString());
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        btn_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMediaLauncher.launch("image/*");
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImageToServer(imageUri,id);
                finish();
            }
        });

    }

    private void uploadImageToServer(Uri imageUri, String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL) // Thay thế "your_base_url" bằng địa chỉ cơ sở của server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServicesComic apiService = retrofit.create(ServicesComic.class);

        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            if (inputStream != null){
                File file = createFileFromInputStream(inputStream);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

                Log.d("zzzz", "uploadImageToServer: " + imagePart);

                Call<ResponseBody> call = apiService.uploadImage(imagePart, id);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            // Xử lý khi upload thành công
                            Log.d("Upload", "Upload thành công!");
                        } else {
                            // Xử lý khi upload thất bại
                            Log.d("Upload", "Upload thất bại!");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // Xử lý khi yêu cầu bị lỗi
                        Log.d("Upload", "Lỗi: " + t.getMessage());
                    }
                });

            }
            // Gửi yêu cầu lên server

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
        }
    }
}