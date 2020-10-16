package com.first.mistrichacha_application.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.first.mistrichacha_application.Activity.ProductInfoActivity;
import com.first.mistrichacha_application.Model.FinalCategoryModel;
import com.first.mistrichacha_application.Model.ProductModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.MyViewHolder> {
    ArrayList<ProductModel.relatedproductInfo> al;
    Context context;

    public RelatedProductAdapter(ArrayList<ProductModel.relatedproductInfo> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCategory ;
        TextView tvName, tvPrice , tvDescription , tvStock , tvDiscount, tyType;
        LinearLayout llProduct , llTimer;

        public MyViewHolder(View view) {
            super(view);
            imgCategory = (ImageView) view.findViewById(R.id.imgCategory);
            tvName = (TextView) view.findViewById(R.id.tvname);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvDescription = (TextView) view.findViewById(R.id.tvDescrption);
            tvStock = (TextView) view.findViewById(R.id.tvStock);
            tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
            llProduct = (LinearLayout) view.findViewById(R.id.llProduct);
            llTimer = (LinearLayout) view.findViewById(R.id.llTimer);


        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_image, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final ProductModel.relatedproductInfo data = al.get(position);

        Picasso.with(context).load(data.thumbnail).into(holder.imgCategory);

        holder.tvName.setText(data.name);
        holder.tvPrice.setText(data.price);
        holder.tvDescription.setText("Type - "+data.type);
        holder.tvDescription.setVisibility(View.GONE);

        if(data.discount.equals("0")){
            holder.tvDiscount.setText("Fresh - No Discount");
        }else{
            holder.tvDiscount.setText(data.discount+"% Off");
        }

        holder.llTimer.setVisibility(View.GONE);

        if(data.stock.equals("")){
            holder.tvStock.setText("Out Of Stock");
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
}
