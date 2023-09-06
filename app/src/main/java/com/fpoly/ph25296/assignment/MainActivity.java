package com.fpoly.ph25296.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.fragment.Admin_quanly_fragment;
import com.fpoly.ph25296.assignment.fragment.User_listcomic_fragment;
import com.fpoly.ph25296.assignment.fragment.User_profile_fragment;
import com.fpoly.ph25296.assignment.model.UsersModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    Admin_quanly_fragment admin_quanly_fragment = new Admin_quanly_fragment();
    User_listcomic_fragment user_listcomic_fragment = new User_listcomic_fragment();
    User_profile_fragment user_profile_fragment = new User_profile_fragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        UsersModel usersModel = (UsersModel) intent.getSerializableExtra("ObjUser");

        if(usersModel.getGroup().equals("Admin")){
            RenderAdmin();
        }else {
            RenderUser();
        }



    }

    private void RenderUser(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.inflateMenu(R.menu.bottom_nav_user);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,user_listcomic_fragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("switch", "onNavigationItemSelected: " + item.getItemId() +"    /" +R.id.nav_list);
                if(item.getItemId() == R.id.nav_list){
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,user_listcomic_fragment).commit();
                    return  true;
                }
                if(item.getItemId() == R.id.nav_Signout){
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,user_profile_fragment).commit();
                    return  true;
                }
                return false;
            }
        });
    }

    private void RenderAdmin(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.inflateMenu(R.menu.bottom_nav_ad);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,admin_quanly_fragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("switch", "onNavigationItemSelected: " + item.getItemId() +"    /" +R.id.nav_list);
                if(item.getItemId() == R.id.nav_Manager){
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,admin_quanly_fragment).commit();
                    return  true;
                }
                if(item.getItemId() == R.id.nav_Signout){
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,user_profile_fragment).commit();
                    return  true;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "Bạn không thể quay lại ở màn hình này", Toast.LENGTH_SHORT).show();
    }
}