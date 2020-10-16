package com.first.mistrichacha_application.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.first.mistrichacha_application.Activity.ShopProductsActivity;
import com.first.mistrichacha_application.Model.LatestProductModel;
import com.first.mistrichacha_application.Model.StoreModel;
import com.first.mistrichacha_application.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoreAdapter  extends RecyclerView.Adapter<StoreAdapter.MyViewHolder> {
    ArrayList<StoreModel.store_list> al;
    Context context;

    public StoreAdapter(ArrayList<StoreModel.store_list> al, Context context) {
        this.al = al;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imgStore ;
        TextView tvName, tvShopEmail , tvShopMobile , tvShopAddress ,  tvRegnumbe;
        LinearLayout llProduct ;

        public MyViewHolder(View view) {
            super(view);
            imgStore = (ImageView) view.findViewById(R.id.imgStore);
            tvName = (TextView) view.findViewById(R.id.tvShopname);
            tvShopEmail = (TextView) view.findViewById(R.id.tvShopContact);
            tvShopMobile = (TextView) view.findViewById(R.id.tvShopMobile);
            tvShopAddress = (TextView) view.findViewById(R.id.tvShopAddress);
            tvRegnumbe = (TextView) view.findViewById(R.id.tvRegNumber);
            llProduct = (LinearLayout)view.findViewById(R.id.llProduct);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.store_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final StoreModel.store_list data = al.get(position);


/*
     Picasso.with(context).load(data.thumbnail).into(holder.imgCategory);
*/

        if(data.photo.equals("")){
         //   holder.imgStore.setImageResource(R.drawable.addimage);
        }else{
            Picasso.with(context).load(data.photo).into(holder.imgStore);
        }

        holder.tvName.setText(data.name);
        holder.tvShopEmail.setText(data.email);
        holder.tvShopMobile.setText(data.shop_number);
        holder.tvShopAddress.setText(data.shop_address);
        holder.tvRegnumbe.setText("Register No - "+data.reg_number);

        holder.llProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, ShopProductsActivity.class);
                in.putExtra("name",data.name);
                in.putExtra("id",data.id);
                context.startActivity(in);
                ((Activity) context).overridePendingTransition(R.anim.left_in, R.anim.left_out);

            }
        });

        /*   holder.tvPrice.setText(data.price);*/

    }


    @Override
    public int getItemCount() {
        return al.size();
    }
}
