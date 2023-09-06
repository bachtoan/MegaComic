package com.fpoly.ph25296.assignment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServiceUsers;
import com.fpoly.ph25296.assignment.ServicesComic;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.ComicModel;
import com.fpoly.ph25296.assignment.model.UsersModel;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUpActivity extends AppCompatActivity {
    EditText edt_name, edt_email;
    TextInputLayout edt_pass, edt_repass;
    AppCompatButton appCompatButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edt_name = findViewById(R.id.ed_DN_username);
        edt_email = findViewById(R.id.ed_email);
        edt_pass = findViewById(R.id.ed_DN_password1);
        edt_repass = findViewById(R.id.ed_DN_password);
        appCompatButton = findViewById(R.id.btn_sign_in);

        appCompatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edt_pass.getEditText().getText().toString().equals("") || edt_name.getText().toString().length() < 1){
                    Toast.makeText(SignUpActivity.this, "Tài khoản - Mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                    return;
                }



                if(edt_pass.getEditText().getText().toString().equals(edt_repass.getEditText().getText().toString())){
                    UsersModel usersModel = new UsersModel(edt_name.getText().toString(), edt_pass.getEditText().getText().toString(), edt_email.getText().toString());
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(API_URL.GET_URL )
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    // Tạo service API
                    ServiceUsers serviceUsers = retrofit.create(ServiceUsers.class);

                    // Gửi yêu cầu POST lên server
                    Call<UsersModel> call = serviceUsers.addUser(usersModel);
                    call.enqueue(new Callback<UsersModel>() {
                        @Override
                        public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                            if (response.isSuccessful()) {
                                UsersModel createdComic = response.body();

                            } else {
                                Log.e("Create Comic", "Request failed");
                            }
                        }

                        @Override
                        public void onFailure(Call<UsersModel> call, Throwable t) {
                            // Xử lý khi yêu cầu bị lỗi
                            // Log.e("Create Comic", "Request error", t);
                        }
                    });
                }
            }
        });

    }
}