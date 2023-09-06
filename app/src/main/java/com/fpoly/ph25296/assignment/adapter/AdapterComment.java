package com.fpoly.ph25296.assignment.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fpoly.ph25296.assignment.R;
import com.fpoly.ph25296.assignment.ServiceUsers;
import com.fpoly.ph25296.assignment.ServicesComic;
import com.fpoly.ph25296.assignment.model.API_URL;
import com.fpoly.ph25296.assignment.model.CommentModel;
import com.fpoly.ph25296.assignment.model.UsersModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.MyViewHolder> {
    List<CommentModel> commentModels;
    Context context;


    public AdapterComment(List<CommentModel> commentModels, Context context) {
        this.commentModels = commentModels;
        this.context = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvName.setText(commentModels.get(position).getName());
        holder.tvComment.setText(commentModels.get(position).getContent());
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER",MODE_PRIVATE);

        String uid = sharedPreferences.getString("id","");

        String imageUrl = API_URL.GET_IMAGE + commentModels.get(position).getImage();
        if(commentModels.get(position).getImage().length() > 5) {
            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .into(holder.comment_image);
        }

        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(commentModels.get(position).getUserId().equals(uid)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("Xóa đối tượng");
                    builder.setMessage("Bạn có chắc chắn muốn xóa cmt này?");
                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DeleteComment(commentModels.get(position).get_id(), commentModels.get(position).getComicId());
                            commentModels.remove(commentModels.get(position));

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
                }else{
                    Toast.makeText(context, "Bạn chỉ có thể thao tác với comment của mình", Toast.LENGTH_SHORT).show();
                }
                return true;

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView comment_image;
        TextView tvName,tvComment;
        CardView layout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.comment_tvname);
            tvComment = itemView.findViewById(R.id.comment_tvcomment);
            comment_image = itemView.findViewById(R.id.comment_img);
            layout = itemView.findViewById(R.id.cardview_comment);
        }
    }

    private void DeleteComment(String commentId, String comicId ){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL.GET_URL) // Địa chỉ cơ sở của API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CommentModel commentModel = new CommentModel(commentId, comicId);

        ServicesComic apiService = retrofit.create(ServicesComic.class);
        Call<CommentModel> call = apiService.delete(commentModel);
        call.enqueue(new Callback<CommentModel>() {
            @Override
            public void onResponse(Call<CommentModel> call, Response<CommentModel> response) {
                Toast.makeText(context, "Xoá thành công", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<CommentModel> call, Throwable t) {
                Log.d("zzzzz", "onFailure: " + t);
            }
        });


    }
}
