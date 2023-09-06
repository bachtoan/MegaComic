package com.fpoly.ph25296.assignment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.activities.AddComicActivity;
import com.fpoly.ph25296.assignment.activities.AddCoverActivity;
import com.fpoly.ph25296.assignment.activities.DetailsActivity;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.ComicModel;

import java.util.ArrayList;

public class AdapterListComic_GV extends BaseAdapter {
    Context context;
    ArrayList<ComicModel> comicModels = new ArrayList<>();

    public AdapterListComic_GV(Context context, ArrayList<ComicModel> comicModels) {
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
            view = inflater.inflate(R.layout.item_comic_gridview,viewGroup,false);
        }

        TextView tv_name = view.findViewById(R.id.item_gv_name);
        TextView tv_publish = view.findViewById(R.id.item_gv_publish);
        TextView tv_author = view.findViewById(R.id.item_gv_author);
        ImageView img_cover = view.findViewById(R.id.item_gv_image);

        tv_name.setText(comicModels.get(i).getName());
        tv_author.setText(comicModels.get(i).getAuthor());
        tv_publish.setText(comicModels.get(i).getId_group());

        String imageUrl = API_URL.GET_IMAGE + comicModels.get(i).getCover();
        if(comicModels.get(i).getCover().length() > 5){
            Glide.with(context)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerInside()
                    .into(img_cover);
        }


        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("ObjComic", comicModels.get(i));
                context.startActivity(intent);
            }
        });




        return view;
    }
}
