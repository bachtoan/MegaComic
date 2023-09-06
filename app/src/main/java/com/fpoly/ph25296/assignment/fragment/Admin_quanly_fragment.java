package com.fpoly.ph25296.assignment.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServicesComic;
import com.fpoly.ph25296.assignment.activities.AddComicActivity;
import com.fpoly.ph25296.assignment.adapter.AdapterListComic;
import com.fpoly.ph25296.assignment.adapter.AdapterSpiner;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.ComicModel;
import com.fpoly.ph25296.assignment.model.Group;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Admin_quanly_fragment extends Fragment {
    Button button;
    ListView lvListComic;

    ArrayList<ComicModel> comicModels = new ArrayList<>();
    ArrayList<Group> listGroup = new ArrayList<>();
    Retrofit retrofit;
    ServicesComic servicesComic;
    AdapterSpiner adapterSpiner;

    AdapterListComic adapterListComic;

    Calendar selectedDateCalendar = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_manager,container,false);
        button = view.findViewById(R.id.btn_add_comic);
        lvListComic = view.findViewById(R.id.lv_listComic);

        LoadGroup();
        LoadListComic();



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_addcomic);
                EditText edtName = dialog.findViewById(R.id.dialog_name);
                EditText edtAuthor = dialog.findViewById(R.id.dialog_author);
                EditText edtDate = dialog.findViewById(R.id.edtDate);
                Spinner spnGroup = dialog.findViewById(R.id.dialog_spiner);
                EditText edtDes = dialog.findViewById(R.id.dialog_des);
                Button btnAdd = dialog.findViewById(R.id.btn_add);

                adapterSpiner = new AdapterSpiner(getContext(),listGroup);
                spnGroup.setAdapter(adapterSpiner);

                edtDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePickerDialog(edtDate);
                    }
                });

                spnGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        // Xử lý khi không có item nào được chọn
                    }
                });


                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Object selectedObject = spnGroup.getSelectedItem();
                        Group group = (Group) selectedObject;

                        ComicModel comicModel = new ComicModel(
                                edtName.getText().toString(),
                                edtAuthor.getText().toString(),
                                group.get_id(),
                                edtDes.getText().toString(),
                                formatDate(selectedDateCalendar));

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(API_URL.GET_URL )
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        // Tạo service API
                        ServicesComic comicService = retrofit.create(ServicesComic.class);

                        // Gửi yêu cầu POST lên server
                        Call<ComicModel> call = comicService.postComic(comicModel);
                        call.enqueue(new Callback<ComicModel>() {
                            @Override
                            public void onResponse(Call<ComicModel> call, Response<ComicModel> response) {
                                if (response.isSuccessful()) {
                                    ComicModel createdComic = response.body();

                                } else {
                                     Log.e("Create Comic", "Request failed");
                                }
                            }

                            @Override
                            public void onFailure(Call<ComicModel> call, Throwable t) {
                                // Xử lý khi yêu cầu bị lỗi
                                // Log.e("Create Comic", "Request error", t);
                            }
                        });
                        LoadListComic();
                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }


    private void showDatePickerDialog(EditText edtDate) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        selectedDateCalendar.set(Calendar.YEAR, year);
                        selectedDateCalendar.set(Calendar.MONTH, month);
                        selectedDateCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        // Hiển thị ngày đã chọn trong EditText
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String selectedDate = sdf.format(selectedDateCalendar.getTime());
                        edtDate.setText(selectedDate);
                    }
                },
                selectedDateCalendar.get(Calendar.YEAR),
                selectedDateCalendar.get(Calendar.MONTH),
                selectedDateCalendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private String formatDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private void LoadGroup(){
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        servicesComic = retrofit.create(ServicesComic.class);
        Call<ArrayList<Group>> call = servicesComic.getGroup();
        call.enqueue(new Callback<ArrayList<Group>>() {
            @Override
            public void onResponse(Call<ArrayList<Group>> call, Response<ArrayList<Group>> response) {
                ArrayList<Group> products = response.body();
                listGroup = products;
            }

            @Override
            public void onFailure(Call<ArrayList<Group>> call, Throwable throwable) {
                Log.d("group", "onFailure: " + throwable);
            }
        });
    }
    private void LoadListComic(){
        retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        servicesComic = retrofit.create(ServicesComic.class);
        Call<ArrayList<ComicModel>> call = servicesComic.getComic();
        call.enqueue(new Callback<ArrayList<ComicModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ComicModel>> call, Response<ArrayList<ComicModel>> response) {
                ArrayList<ComicModel> list = response.body();
                comicModels = list;
                adapterListComic = new AdapterListComic(getContext(),comicModels);
                lvListComic.setAdapter(adapterListComic);
                adapterListComic.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<ComicModel>> call, Throwable t) {

            }
        });

    }
}
