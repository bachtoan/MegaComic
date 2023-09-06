package com.fpoly.ph25296.assignment.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.activities.DetailsActivity;
import com.fpoly.ph25296.assignment.model.API_URL;

import java.util.ArrayList;
import java.util.List;

public class ReadAdapter extends BaseAdapter {
    List list = new ArrayList();
    Context context;

    public ReadAdapter(List list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.item_read, viewGroup, false);
        }
        ImageView img = view.findViewById(R.id.img_read);

        String imageUrl = API_URL.GET_IMAGE + list.get(i);
        if (list.get(i).toString().length() > 5) {
            Glide.with(context)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerInside()
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            // Đặt ảnh vào ImageView
                            img.setImageDrawable(resource);

                            // Đặt chiều rộng của ImageView bằng match_parent
                            img.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;

                            // Tính toán chiều cao mới cho ImageView dựa trên tỷ lệ của ảnh
                            int originalWidth = resource.getIntrinsicWidth();
                            int originalHeight = resource.getIntrinsicHeight();
                            int newHeight = (int) (originalHeight * (float) img.getWidth() / originalWidth);
                            img.getLayoutParams().height = newHeight;
                            img.requestLayout(); // Cập nhật layout của ImageView
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Xử lý khi ảnh bị xóa đi (nếu có)
                        }
                    });
        }
        return view;
    }
}
