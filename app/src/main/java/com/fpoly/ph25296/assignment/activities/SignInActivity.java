package com.fpoly.ph25296.assignment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fpoly.ph25296.assignment.MainActivity;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServiceUsers;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.UsersModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        EditText edt_username = findViewById(R.id.ed_DN_username);
        TextInputLayout edt_passwrd = findViewById(R.id.ed_DN_password);
        Button btn_signin = findViewById(R.id.btn_sign_in);
        CheckBox ckb_save = findViewById(R.id.cb_luumk);
        TextView tv_signup = findViewById(R.id.tv_signup);

        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });



        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edt_username.getText().toString();
                String pass = edt_passwrd.getEditText().getText().toString();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API_URL.GET_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                ServiceUsers serviceUsers= retrofit.create(ServiceUsers.class);
                UsersModel usersModel = new UsersModel(username, pass);
                Call<UsersModel> call = serviceUsers.login(usersModel);

                call.enqueue(new Callback<UsersModel>() {
                    @Override
                    public void onResponse(Call<UsersModel> call, Response<UsersModel> response) {
                        if (response.isSuccessful()) {
                            UsersModel result = response.body();
                            SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name", result.getName());
                            editor.putString("id", result.get_id());
                            editor.putString("group", result.getGroup());
                            editor.putString("image",result.getImage());
                            editor.apply();
                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            intent.putExtra("ObjUser", result);
                            startActivity(intent);
                            Toast.makeText(SignInActivity.this, "Đăng nhập thành công vói tư cách " + result.getGroup(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignInActivity.this, "Sai tên đăng nhập hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UsersModel> call, Throwable t) {
                        Log.d("zzzz", "onFailure: " + t);
                    }
                });
            }
        });
    }
}