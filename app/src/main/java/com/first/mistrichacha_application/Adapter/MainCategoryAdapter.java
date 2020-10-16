package com.first.mistrichacha_application.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Activity.SubCategoryActivity;
import com.first.mistrichacha_application.Model.CategoryModel;
import com.first.mistrichacha_application.Model.FinalCategoryModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.MyViewHolder> {
    ArrayList<FinalCategoryModel.Category_Data_list> al;
    Context context;

    public MainCategoryAdapter(ArrayList<FinalCategoryModel.Category_Data_list> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView imgCategory ;
        LinearLayout llContainer ;
        TextView tvname  ;

        public MyViewHolder(View view) {
            super(view);
            imgCategory = (CircleImageView) view.findViewById(R.id.CircleImageView);
            llContainer = (LinearLayout) view.findViewById(R.id.llContainer);
            tvname = (TextView) view.findViewById(R.id.cat_name);

        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_image, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final FinalCategoryModel.Category_Data_list data = al.get(position);

    if(data.image.equals("")){
        holder.imgCategory.setImageResource(R.drawable.teddy);
    }else{
        Picasso.with(context).load(data.image).into(holder.imgCategory);
    }

    holder.tvname.setText(data.name);

        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, SubCategoryActivity.class);
                in.putExtra("subCategory",data.name);
                in.putExtra("slug",data.slug);
                context.startActivity(in);
                ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

    }

    @Override
    public int getItemCount() {
        return al.size();
    }
}
