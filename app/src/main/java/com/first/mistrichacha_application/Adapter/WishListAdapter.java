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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Activity.ProductInfoActivity;
import com.first.mistrichacha_application.Interface.DeletInterface;
import com.first.mistrichacha_application.Model.LatestProductModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.MyViewHolder> {
    ArrayList<LatestProductModel.datalist> al;
    Context context;

    public WishListAdapter(ArrayList<LatestProductModel.datalist> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory ;
        LinearLayout llProduct ;
        TextView tvName, tvPriviousPrice , tvPrice , tvDiscount,tvRating,tvStock;

        public MyViewHolder(View view) {
            super(view);
            imgCategory = (ImageView) view.findViewById(R.id.imgCategory);
            llProduct = (LinearLayout) view.findViewById(R.id.llProduct);
            tvName = (TextView) view.findViewById(R.id.tvname);
            tvPriviousPrice = (TextView) view.findViewById(R.id.tvPriviousPrice);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvDiscount=(TextView)view.findViewById(R.id.tvDiscount);
            tvRating = (TextView) view.findViewById(R.id.tvRating);
            tvStock = (TextView) view.findViewById(R.id.tvStock);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.latestproduct_item_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final LatestProductModel.datalist data = al.get(position);

        if(data.thumbnail.equals("")){
            holder.imgCategory.setImageResource(R.drawable.addimage);
        }else{
            Picasso.with(context).load(data.thumbnail).into(holder.imgCategory);
        }

        holder.tvName.setText(data.name);
        holder.tvPriviousPrice.setText(data.previous_price);
        holder.tvPriviousPrice.setPaintFlags(holder.tvPriviousPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        if(data.discount.equals("0")){
            holder.tvDiscount.setText("New ");
        }else{
            holder.tvDiscount.setText(data.discount+"% Off");
        }
        holder.tvPrice.setText(data.price);
         holder.tvRating.setText(data.rating);

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
                in.putExtra("like","favourite");
                in.putExtra("position",position);
               ((Activity)context).startActivity(in);

                ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });
    }


    @Override
    public int getItemCount() {
        return al.size();
    }
}
