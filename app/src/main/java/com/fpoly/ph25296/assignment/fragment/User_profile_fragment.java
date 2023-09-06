package com.fpoly.ph25296.assignment.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpoly.ph25296.assignment.MainActivity;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServiceUsers;
import com.fpoly.ph25296.assignment.ServicesComic;
import com.fpoly.ph25296.assignment.activities.DetailsActivity;
import com.fpoly.ph25296.assignment.activities.SignInActivity;
import com.fpoly.ph25296.assignment.adapter.AdapterComment;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.ComicModel;
import com.fpoly.ph25296.assignment.model.CommentModel;
import com.fpoly.ph25296.assignment.model.UsersModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
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

public class User_profile_fragment extends Fragment {
    ImageView imageView;
    Button btn_image, btn_logout;

    TextView tv_name,tv_group;
    String id;

    private ActivityResultLauncher<String> pickImageLauncher;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        imageView = view.findViewById(R.id.user_image);
        btn_image = view.findViewById(R.id.btn_doi_anh);
        tv_name = view.findViewById(R.id.tv_name);
        tv_group = view.findViewById(R.id.tv_group);
        btn_logout = view.findViewById(R.id.btn_dangxuat_hsnv);


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER",MODE_PRIVATE);

        id = sharedPreferences.getString("id","");

        tv_name.setText(sharedPreferences.getString("name",""));
        tv_group.setText(sharedPreferences.getString("group",""));
        LoadUser();
        // Khởi tạo ActivityResultLauncher
        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        // Xử lý kết quả khi đã chọn ảnh thành công
                        if (result != null) {
                            // Hiển thị ảnh đã chọn trong ImageView bằng Glide
                            Glide.with(requireContext())
                                    .load(result)
                                    .centerCrop()
                                    .into(imageView);
                            uploadImageToServer(result,id);
                        }
                    }
                });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mở màn hình chọn ảnh
                pickImageLauncher.launch("image/*");
            }
        });
    }

    private void uploadImageToServer(Uri imageUri, String id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL) // Thay thế "your_base_url" bằng địa chỉ cơ sở của server
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServiceUsers apiService = retrofit.create(ServiceUsers.class);

        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);

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
                            Log.d("Upload", "Upload thành công!");
                        } else {
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

            File file = new File(getContext().getCacheDir(), uniqueFileName);
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

    private void LoadUser(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServiceUsers servicesComic = retrofit.create(ServiceUsers.class);
        Call<UsersModel> call = servicesComic.getOneUser(id);
        call.enqueue(new Callback<UsersModel>() {
            @Override
            public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                if (response.isSuccessful()) {
                    UsersModel comicModel = response.body();
                    Log.d("zzzz", "onResponse: " + comicModel.getImage());
                    String imageUrl = API_URL.GET_IMAGE + comicModel.getImage();
                    Glide.with(requireContext())
                            .load(imageUrl)
                            .centerCrop()
                            .into(imageView);
                } else {
                    Log.d("zzzz", "eles: " + response);
                }
            }

            @Override
            public void onFailure(Call<UsersModel> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Log.d("zzzz", "onFailure: " + t);
            }
        });
    }
}
