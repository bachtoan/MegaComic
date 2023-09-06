package com.fpoly.ph25296.assignment.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServicesComic;
import com.fpoly.ph25296.assignment.adapter.AdapterListComic;
import com.fpoly.ph25296.assignment.adapter.AdapterListComic_GV;
import com.fpoly.ph25296.assignment.adapter.AdapterSpiner;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.ComicModel;
import com.fpoly.ph25296.assignment.model.Group;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class User_listcomic_fragment extends Fragment {
    Retrofit retrofit;
    GridView gridView;
    ArrayList<ComicModel> comicModels = new ArrayList<>();
    ArrayList<Group> listGroup = new ArrayList<>();
    ServicesComic servicesComic;
    AdapterSpiner adapterSpiner;

    AdapterListComic_GV adapterListComic;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_user_listcomic,container,false);
        gridView = view.findViewById(R.id.gv_listcomic);
        LoadListComic();
        return view;
    }

    private void LoadListComic(){
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServicesComic servicesComic = retrofit.create(ServicesComic.class);
        Call<ArrayList<ComicModel>> call = servicesComic.getComic();
        call.enqueue(new Callback<ArrayList<ComicModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ComicModel>> call, Response<ArrayList<ComicModel>> response) {
                ArrayList<ComicModel> list = response.body();
                comicModels = list;
                adapterListComic = new AdapterListComic_GV(getContext(),comicModels);
                gridView.setAdapter(adapterListComic);
                adapterListComic.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<ComicModel>> call, Throwable t) {

            }
        });

    }
}
