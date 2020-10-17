package com.first.mistrichacha_application.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Activity.ProductInfoActivity;
import com.first.mistrichacha_application.Model.SubCategoryModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    ArrayList<SubCategoryModel.datalist> al;
    ArrayList<SubCategoryModel.datalist> alfilter;
    Context context;

    public SubCategoryAdapter(ArrayList<SubCategoryModel.datalist> al, Context context) {
        this.al = al;
        this.context = context;
        this.alfilter = new ArrayList<SubCategoryModel.datalist>();
        this.alfilter.addAll(al);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory ;
        TextView tvName, tvPriviousPrice , tvPrice , tvRating , tvStock , tvDiscount ;
        LinearLayout llProduct ;

        public MyViewHolder(View view) {
            super(view);
            imgCategory = (ImageView) view.findViewById(R.id.imgCategory);
            tvName = (TextView) view.findViewById(R.id.tvname);
            tvPriviousPrice = (TextView) view.findViewById(R.id.tvPriviousPrice);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            llProduct = (LinearLayout) view.findViewById(R.id.llProduct);
            tvRating = (TextView) view.findViewById(R.id.tvRating);
            tvStock = (TextView) view.findViewById(R.id.tvStock);
            tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.latestproduct_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final SubCategoryModel.datalist data = al.get(position);

        String path =  "https://mistrichacha.com/assets/images/thumbnails/";
        Picasso.with(context).load(path+data.thumbnail).into(holder.imgCategory);

        holder.tvName.setText(data.name);
        holder.tvPriviousPrice.setText(data.previous_price);
        holder.tvPriviousPrice.setPaintFlags(holder.tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.tvPrice.setText(data.price);
         holder.tvRating.setText(data.rating);


        if(data.discount.equals("0")){
            holder.tvDiscount.setText("New ");
        }else{
            holder.tvDiscount.setText(data.discount+" % Off");
        }

        if(data.stock.equals("")){
            holder.tvStock.setText("Currently Not Available");
            holder.tvStock.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.tvStock.setText("In Stock");
            holder.tvStock.setTextColor(Color.parseColor("#008000"));
        }

        holder.llProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, ProductInfoActivity.class);
                in.putExtra("product_id",data.id);
                in.putExtra("like","");
                context.startActivity(in);
                ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });
    }

    @Override
    public int getItemCount() {
        return al.size();
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        al.clear();
        if (charText.length() == 0) {
            al.addAll(alfilter);
        } else {
            for (SubCategoryModel.datalist wp : alfilter) {
                if (wp.name.toLowerCase(Locale.getDefault()).contains(charText)) {
                    al.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }




}
