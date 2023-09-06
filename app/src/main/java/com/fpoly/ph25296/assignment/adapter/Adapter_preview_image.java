package com.fpoly.ph25296.assignment.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fpoly.ph25296.assignment.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter_preview_image extends BaseAdapter {
    Context context;
    ArrayList list = new ArrayList();

    public Adapter_preview_image(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
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


    public static class ViewOfItem{
        ImageView item_image;
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        ViewOfItem viewOfItem;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_preview_image,parent,false);
            viewOfItem = new ViewOfItem();
            viewOfItem.item_image = convertView.findViewById(R.id.item_preview_image);
            viewOfItem.textView = convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewOfItem);
        }else{
            viewOfItem = (ViewOfItem) convertView.getTag();
        }
        Log.d("adapter", "getCount: " + getCount());
        Log.d("adapter", "adapter list: " + list);

        viewOfItem.textView.setText(list.get(position)+"");

            Glide.with(parent)
                    .load(list.get(position))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(new RequestOptions().centerCrop())
                    .into(viewOfItem.item_image);




        return convertView;
    }
}
