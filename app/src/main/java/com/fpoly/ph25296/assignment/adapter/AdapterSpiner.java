package com.fpoly.ph25296.assignment.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.model.Group;

import java.util.ArrayList;

public class AdapterSpiner extends BaseAdapter {
    Context context;
    ArrayList<Group> list = new ArrayList();

    public AdapterSpiner(Context context, ArrayList<Group> list) {
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
        TextView textView;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        ViewOfItem viewOfItem;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_spinner,viewGroup,false);
            viewOfItem = new ViewOfItem();
            viewOfItem.textView = convertView.findViewById(R.id.tv_spn_text);
            convertView.setTag(viewOfItem);
        }else{
            viewOfItem = (ViewOfItem) convertView.getTag();
        }

        viewOfItem.textView.setText(list.get(i).getName()+"");

        return convertView;
    }
}
