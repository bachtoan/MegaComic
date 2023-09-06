package com.fpoly.ph25296.assignment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ListView;

import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.adapter.ReadAdapter;
import com.fpoly.ph25296.assignment.model.ComicModel;

import java.util.List;

public class ReadActivity extends AppCompatActivity {
    ComicModel comicModel;
    ReadAdapter readAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        ListView listView = findViewById(R.id.lv_read);
        Intent intent = getIntent();
        comicModel = (ComicModel) intent.getSerializableExtra("ObjComic");
        readAdapter = new ReadAdapter(comicModel.getImages(), ReadActivity.this);
        listView.setAdapter(readAdapter);
    }
}