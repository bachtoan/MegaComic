package com.fpoly.ph25296.assignment.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServicesComic;
import com.fpoly.ph25296.assignment.activities.AddComicActivity;
import com.fpoly.ph25296.assignment.activities.AddCoverActivity;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.ComicModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterListComic extends BaseAdapter {
    Context context;
    ArrayList<ComicModel> comicModels = new ArrayList<>();

    public AdapterListComic(Context context, ArrayList<ComicModel> comicModels) {
        this.context = context;
        this.comicModels = comicModels;
    }

    @Override
    public int getCount() {
        return comicModels.size();
    }

    @Override
    public Object getItem(int i) {
        return comicModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_comic,viewGroup,false);
        }

        TextView tv_name = view.findViewById(R.id.name_comic);
        TextView tv_group = view.findViewById(R.id.group_comic);
        TextView tv_publish = view.findViewById(R.id.publish_comic);
        ImageView img_cover = view.findViewById(R.id.img_comic);

        Log.d("adt", "getView: " + comicModels.get(i).getName());

        tv_name.setText(comicModels.get(i).getName());
        tv_group.setText(comicModels.get(i).getAuthor());
        tv_publish.setText(comicModels.get(i).getPublish());

        String imageUrl = API_URL.GET_IMAGE + comicModels.get(i).getCover();
        if(comicModels.get(i).getCover().length() > 5){
            Glide.with(context)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerInside()
                    .into(img_cover);
        }


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddComicActivity.class);
                intent.putExtra("id",comicModels.get(i).get_id());
                context.startActivity(intent);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                String id = comicModels.get(i).get_id();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Xóa đối tượng");
                builder.setMessage("Bạn có chắc chắn muốn xóa truyện này?");
                builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Log.d("zid", "onClick: "+ id);
                        DeleteComic(id);
                        comicModels.remove(comicModels.get(i));
                        notifyDataSetChanged();

                        dialogInterface.dismiss(); // Đóng dialog
                    }
                });
                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss(); // Đóng dialog
                    }
                });
                builder.create().show();
                return true;
            }
        });

        img_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(context, AddCoverActivity.class);
                intent.putExtra("id",comicModels.get(i).get_id());
                context.startActivity(intent);
            }
        });


        return view;
    }

    private void DeleteComic(String id){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL) // Địa chỉ cơ sở của API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ComicModel comicModel = new ComicModel(id);
        Log.d("sss", "DeleteComic: " + comicModel);

        ServicesComic apiService = retrofit.create(ServicesComic.class);
        Call<Void> call = apiService.deleteComic(comicModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xoá thành công
                    Log.d("Delete", "Xoá thành công");
                } else {
                    // Xoá không thành công
                    Log.d("Delete", "Xoá không thành công");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Xử lý lỗi khi gửi yêu cầu
                Log.e("Delete", "Lỗi: " + t.getMessage());
            }
        });
    }
}
